package com.Eudaimonia.DigitalJournalApp.configuration;

import java.util.Collection;

import com.Eudaimonia.DigitalJournalApp.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Setter
@Getter
@RequiredArgsConstructor
public class UserInfoUserDetails implements UserDetails {
    private Long id;
    private String name;
    private String password;

    public UserInfoUserDetails(User user) {
        id = user.getId();
        name = user.getName();
        password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
