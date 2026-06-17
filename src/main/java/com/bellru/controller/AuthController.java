package com.bellru.controller;

import com.bellru.dto.ChangePasswordRequest;
import com.bellru.dto.LoginRequest;
import com.bellru.dto.LoginResponse;
import com.bellru.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest req, Authentication authentication) {
        authService.changePassword(authentication.getName(), req.getOldPassword(), req.getNewPassword());
    }
}
