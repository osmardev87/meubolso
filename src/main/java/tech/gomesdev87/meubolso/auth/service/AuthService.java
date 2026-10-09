package tech.gomesdev87.meubolso.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.gomesdev87.meubolso.auth.dto.AuthResponse;
import tech.gomesdev87.meubolso.auth.dto.LoginRequest;
import tech.gomesdev87.meubolso.auth.dto.RegisterRequest;
import tech.gomesdev87.meubolso.exception.RuntimeExetionErro;
import tech.gomesdev87.meubolso.security.JwtService;
import tech.gomesdev87.meubolso.user.User;
import tech.gomesdev87.meubolso.user.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password()));
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user, user.getId());
        return new AuthResponse(token);
    }


    @Transactional(readOnly = true)
    public String newUsers(RegisterRequest request) {
        User user = RegisterRequest.toRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.existsByEmail(request.email())){
            throw new RuntimeExetionErro("Email ja existe!");
        }

        if(userRepository.existsByPhone(request.phone())){
            throw new RuntimeExetionErro("Telefone ja Cadastrado!");
        }
        User newUser = userRepository.save(user);
        return "Bem vindo ao OpenFinace" + newUser.getName();
    }

}
