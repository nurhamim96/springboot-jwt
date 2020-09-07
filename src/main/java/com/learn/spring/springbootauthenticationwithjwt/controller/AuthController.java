package com.learn.spring.springbootauthenticationwithjwt.controller;

import com.learn.spring.springbootauthenticationwithjwt.dto.request.LoginRequest;
import com.learn.spring.springbootauthenticationwithjwt.dto.request.UserRequest;
import com.learn.spring.springbootauthenticationwithjwt.dto.response.JwtAuthResponse;
import com.learn.spring.springbootauthenticationwithjwt.security.JwtTokenProvider;
import com.learn.spring.springbootauthenticationwithjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.ResultSet;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        return ResponseEntity.ok(userService.findByUsername(principal.getName()));
    }
}
