package com.app.us_twogether.domain.authenticaiton;

import com.app.us_twogether.domain.authentication.LoginResponseDTO;
import com.app.us_twogether.domain.authentication.token.RefreshToken;
import com.app.us_twogether.domain.authentication.token.TokenService;
import com.app.us_twogether.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthenticationTokenTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    @Test
    @WithMockUser(username = "john_doe")
    void shouldRefreshToken_whenTokenIsValid() throws Exception {
        String refreshTokenValue = "valid-refresh-token";
        User user = new User("john_doe", "1234");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setUser(user);

        Instant expiryDate = Instant.now().plusSeconds(3600);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                "john_doe",
                "new-access-token",
                "new-refresh-token",
                expiryDate
        );

        when(tokenService.findByToken(refreshTokenValue)).thenReturn(refreshToken);
        when(tokenService.isTokenExpired(any())).thenReturn(false);
        when(tokenService.login(any())).thenReturn(loginResponseDTO);

        mockMvc.perform(post(apiBaseUrl + "/auth/refresh")
                        .header("refreshToken", refreshTokenValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"))
                .andExpect(jsonPath("$.expiryDate").exists());

        verify(tokenService, times(1)).findByToken(refreshTokenValue);
        verify(tokenService, times(1)).isTokenExpired(refreshToken);
        verify(tokenService, times(1)).login(refreshToken.getUser());
    }

    @Test
    @WithMockUser(username = "john_doe")
    void shouldRefreshToken_whenTokenIsInvalid() throws Exception {
        String refreshTokenValue = "expired-refresh-token";

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);

        when(tokenService.findByToken(refreshTokenValue)).thenReturn(refreshToken);
        when(tokenService.isTokenExpired(any())).thenReturn(true);

        mockMvc.perform(post(apiBaseUrl + "/auth/refresh")
                        .header("refreshToken", refreshTokenValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(tokenService, times(1)).findByToken(refreshTokenValue);
        verify(tokenService, times(1)).isTokenExpired(refreshToken);
        verify(tokenService, times(0)).login(refreshToken.getUser());
    }
}
