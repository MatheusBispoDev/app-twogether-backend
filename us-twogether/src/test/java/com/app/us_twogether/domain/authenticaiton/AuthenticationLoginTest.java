package com.app.us_twogether.domain.authenticaiton;

import com.app.us_twogether.domain.authentication.AuthenticationRequestDTO;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void AuthenticationLoginUserSuccessfullyTest() throws Exception {
        User user = new User("john_doe", "1234");

        userService.createUser(user);

        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO(user.getUsername(), "1234");

        ResultActions result = mockMvc.perform(post(apiBaseUrl + "/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.expiryDate").isNotEmpty());
    }

    @Test
    public void AuthenticationLoginUserFailedTest() throws Exception {
        User user = new User("john_doe", "1234");

        userService.createUser(user);

        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO(user.getUsername(), "4321");

        ResultActions result = mockMvc.perform(post(apiBaseUrl + "/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));

        result.andExpect(status().isForbidden());
    }
}
