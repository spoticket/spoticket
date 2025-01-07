package com.spoticket.user.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.spoticket.user.domain.model.UserRole;
import com.spoticket.user.domain.model.entity.User;
import com.spoticket.user.domain.repository.UserRepository;
import com.spoticket.user.dto.request.UserLoginRequestDto;
import com.spoticket.user.dto.request.UserRoleChangeRequestDto;
import com.spoticket.user.dto.request.UserSignupRequestDto;
import com.spoticket.user.dto.request.UserUpdateRequestDto;
import com.spoticket.user.dto.response.UserResponseDto;
import com.spoticket.user.global.exception.CustomException;
import com.spoticket.user.global.exception.ErrorStatus;
import com.spoticket.user.global.util.JwtUtil;
import com.spoticket.user.global.util.ResponseStatus;
import com.spoticket.user.global.util.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.spoticket.user.domain.model.entity.QUser.user;
import static com.spoticket.user.global.util.ResponseStatus.USER_INFO_UPDATE;
import static com.spoticket.user.global.util.ResponseStatus.USER_ROLE_CHANGED;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SuccessResponse<?> createUser(UserSignupRequestDto request) {

        User user = User.of(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.slackId(),
                UserRole.ROLE_USER,
                request.name(),
                request.gender(),
                request.birthday(),
                request.post(),
                request.address(),
                request.addressDetail()
        );
        userRepository.save(user);

        return SuccessResponse.of(ResponseStatus.USER_SIGN_UP);
    }

    public SuccessResponse<?> login(UserLoginRequestDto request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new CustomException(ErrorStatus.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorStatus.USER_NOT_FOUND);
        }

        String token = jwtUtil.createAccessToken(user.getUserId(), user.getRole());

        return SuccessResponse.of(ResponseStatus.USER_LOGIN, token);
    }

    @Transactional
    public SuccessResponse<?> changeRole(UUID currentUserId, String role, UserRoleChangeRequestDto request, UUID targetUserId) {

        // 자기 자신이 아니거나 마스터가 아닐 경우 throw
        if (!role.equals(UserRole.ROLE_MASTER.toString())) {
            if (!currentUserId.equals(targetUserId)) {
                throw new CustomException(ErrorStatus.FORBIDDEN);
            }
        }

        User user = userRepository.findById(targetUserId).orElseThrow(
                () -> new CustomException(ErrorStatus.USER_NOT_FOUND)
        );

        user.roleChange(UserRole.valueOf(request.role()));

        return SuccessResponse.of(USER_ROLE_CHANGED);
    }

    @Transactional(readOnly = true)
    public SuccessResponse<?> selectUserById(UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorStatus.USER_NOT_FOUND)
        );

        UserResponseDto response = new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getGender(),
                user.getBirthday(),
                user.getPost(),
                user.getAddress(),
                user.getAddressDetail(),
                user.getSlackId()
        );

        return SuccessResponse.ok(response);
    }

    @Transactional(readOnly = true)
    public SuccessResponse<?> selectUserList(Predicate predicate, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);
        booleanBuilder.and(user.isDelete.eq(false));

        Page<User> userList = userRepository.findAll(booleanBuilder, pageable);

        return SuccessResponse.ok(toUserResponseDtoPage(userList));
    }

    public Page<UserResponseDto> toUserResponseDtoPage(Page<User> userPage) {
        return userPage.map(user -> new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getGender(),
                user.getBirthday(),
                user.getPost(),
                user.getAddress(),
                user.getAddressDetail(),
                user.getSlackId()
        ));
    }

    @Transactional
    public SuccessResponse<?> update(UUID userId, UserUpdateRequestDto request) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorStatus.USER_NOT_FOUND)
        );

        user.update(
                request.email(),
                !request.password().isEmpty() ? passwordEncoder.encode(request.password()) : request.password(),
                request.slackId(),
                request.name(),
                request.gender(),
                request.birthday(),
                request.post(),
                request.address(),
                request.addressDetail()
        );

        return SuccessResponse.ok(USER_INFO_UPDATE);
    }
}
