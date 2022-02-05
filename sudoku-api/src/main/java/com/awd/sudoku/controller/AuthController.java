package com.awd.sudoku.controller;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.payload.request.AuthRequest;
import com.awd.sudoku.payload.response.JwtResponse;
import com.awd.sudoku.payload.response.MessageResponse;
import com.awd.sudoku.security.jwt.JwtUtils;
import com.awd.sudoku.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername().toLowerCase().trim(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AppUser user = (AppUser) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername().toLowerCase().trim())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username is already in use"));
        }

        AppUser user = new AppUser();
        user.setUsername(registerRequest.getUsername().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
