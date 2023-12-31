package com.Eudaimonia.DigitalJournalApp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.Eudaimonia.DigitalJournalApp.contract.request.AuthRequest;
import com.Eudaimonia.DigitalJournalApp.contract.request.UserRequest;
import com.Eudaimonia.DigitalJournalApp.contract.response.AuthResponse;
import com.Eudaimonia.DigitalJournalApp.contract.response.UserResponse;
import com.Eudaimonia.DigitalJournalApp.service.JwtService;
import com.Eudaimonia.DigitalJournalApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;
    @MockBean private JwtService jwtService;
    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private PasswordEncoder passwordEncoder;

    @Test
    public void testAddUser() throws Exception {
        UserRequest userRequest =
                UserRequest.builder().name("test").email("test@gmail.com").password("pass").build();
        UserResponse response =
                UserResponse.builder()
                        .id(1L)
                        .name("test")
                        .email("test@gmail.com")
                        .password("pass")
                        .build();

        when(userService.addUser(userRequest)).thenReturn(response);
        String path = "/journal/sign-up";
        mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAuthenticateAndGetToken() throws Exception {
        AuthRequest authRequest = AuthRequest.builder().name("test").password("pass").build();
        AuthResponse expectedResponse = AuthResponse.builder().token("token").build();

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(authRequest.getName())).thenReturn(expectedResponse);

        String path = "/login";
        mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().string(""));
    }
}
