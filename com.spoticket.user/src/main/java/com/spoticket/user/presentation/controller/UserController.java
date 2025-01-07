package com.spoticket.user.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.spoticket.user.application.service.UserService;
import com.spoticket.user.domain.model.entity.User;
import com.spoticket.user.dto.request.UserLoginRequestDto;
import com.spoticket.user.dto.request.UserRoleChangeRequestDto;
import com.spoticket.user.dto.request.UserSignupRequestDto;
import com.spoticket.user.global.util.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody UserSignupRequestDto request
    ) {
        return ResponseEntity.ok().body(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserLoginRequestDto request
    ) {
        SuccessResponse<?> jwtTocken = userService.login(request);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtTocken.data())
                .body(jwtTocken);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Role") String role
    ) {
        return ResponseEntity.ok()
                .body("UserId: " + userId + ", Role: " + role);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> roleChange(
            @RequestHeader("X-User-Id") UUID currentUserId,
            @RequestHeader("X-Role") String role,
            @RequestBody UserRoleChangeRequestDto request,
            @PathVariable UUID userId

    ){
        return ResponseEntity.ok()
                .body(userService.changeRole(currentUserId, role, request, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> selectUserById(
            @PathVariable UUID userId
    ){
        return ResponseEntity.ok()
                .body(userService.selectUserById(userId));
    }

    @GetMapping
    public ResponseEntity<?> selectUserList(
            @QuerydslPredicate(root = User.class) Predicate predicate,
            @PageableDefault(sort = "userId", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok()
                .body(userService.selectUserList(predicate, pageable));
    }
}
