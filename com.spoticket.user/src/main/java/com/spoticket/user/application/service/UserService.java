package com.spoticket.user.application.service;

import com.spoticket.user.domain.model.UserRole;
import com.spoticket.user.domain.model.entity.User;
import com.spoticket.user.domain.repository.UserRepository;
import com.spoticket.user.dto.request.UserLoginRequestDto;
import com.spoticket.user.dto.request.UserSignupRequestDto;
import com.spoticket.user.global.exception.CustomException;
import com.spoticket.user.global.exception.ErrorStatus;
import com.spoticket.user.global.util.JwtUtil;
import com.spoticket.user.global.util.ResponseStatus;
import com.spoticket.user.global.util.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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
}
