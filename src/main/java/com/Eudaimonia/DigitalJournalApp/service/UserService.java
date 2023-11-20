package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.Exception.UserNotFoundException;
import com.Eudaimonia.DigitalJournalApp.contract.Request.UserRequest;
import com.Eudaimonia.DigitalJournalApp.contract.Response.UserResponse;
import com.Eudaimonia.DigitalJournalApp.model.User;
import com.Eudaimonia.DigitalJournalApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse addUser(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByName(userRequest.getName().trim());
        if (userOptional.isPresent()){
            throw new UserNotFoundException("User name already exists !");
        }
        User user =
                User.builder()
                        .name(userRequest.getName().trim())
                        .email(userRequest.getEmail().toLowerCase().trim())
                        .password(passwordEncoder.encode(userRequest.getPassword()))
                        .build();
        User saved = userRepository.save(user);
        return UserResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .password(saved.getPassword())
                .build();
    }
}
