package com.spoticket.user.presentation.controller;

import com.spoticket.user.application.service.UserService;
import com.spoticket.user.dto.request.UserLoginRequestDto;
import com.spoticket.user.dto.request.UserSignupRequestDto;
import com.spoticket.user.global.util.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody(required = false) UserSignupRequestDto request
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
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Role") String role
    ) {
        return ResponseEntity.ok().body("UserId: " + userId + ", Role: " + role);
    }
}
