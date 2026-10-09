package tech.gomesdev87.meubolso.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.gomesdev87.meubolso.auth.dto.AuthResponse;
import tech.gomesdev87.meubolso.auth.dto.LoginRequest;
import tech.gomesdev87.meubolso.auth.dto.RegisterRequest;
import tech.gomesdev87.meubolso.auth.service.AuthService;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody
    LoginRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> create(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.newUsers(request));
    }
}
