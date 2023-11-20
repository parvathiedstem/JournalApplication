package com.Eudaimonia.DigitalJournalApp.Configuration;

import com.Eudaimonia.DigitalJournalApp.Exception.UserNotFoundException;
import com.Eudaimonia.DigitalJournalApp.model.User;
import com.Eudaimonia.DigitalJournalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByName(username);
        return userOptional
                .map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
