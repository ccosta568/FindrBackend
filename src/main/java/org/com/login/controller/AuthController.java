package org.com.login.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.com.login.dto.LoginRequest;
import org.com.login.dto.LoginResponse;
import org.com.login.dto.RegisterRequest;
import org.com.login.service.AuthService;
import org.com.login.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        // Authenticate the user (you’d typically validate the password here)

        String token = authService.login(request);

        LoginResponse response = LoginResponse.builder()
                .username(request.getUsername())
                .token(token)
                .build();

        return ResponseEntity.ok(response);
    }
}
