package com.Eudaimonia.DigitalJournalApp.controller;

import com.Eudaimonia.DigitalJournalApp.Exception.UserNotFoundException;
import com.Eudaimonia.DigitalJournalApp.contract.Request.AuthRequest;
import com.Eudaimonia.DigitalJournalApp.contract.Request.UserRequest;
import com.Eudaimonia.DigitalJournalApp.contract.Response.AuthResponse;
import com.Eudaimonia.DigitalJournalApp.contract.Response.UserResponse;
import com.Eudaimonia.DigitalJournalApp.service.JwtService;
import com.Eudaimonia.DigitalJournalApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journal")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse response = userService.addUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateAndGetToken(
            @Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            AuthResponse response = jwtService.generateToken(authRequest.getName());
            return ResponseEntity.ok(response);
        } else {
            throw new UserNotFoundException("Invalid user request !");
        }
    }
}
