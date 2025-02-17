package com.app.us_twogether.domain.authenticaiton;

import com.app.us_twogether.domain.user.*;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthenticationRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserRequestDTO userRequestDTO = new UserRequestDTO("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");

    private final UserResponseDTO userResponseDTO = new UserResponseDTO("john_doe", "John Doe", "john@example.com", "11932178425", "US");

    @Test
    public void shouldAuthenticateRegisterUser_whenCredentialsAreValid() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post(apiBaseUrl + "/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("john_doe"));

        Mockito.verify(userService, times(1)).createUser(Mockito.any(User.class));
    }

    @Test
    public void shouldAuthenticateRegisterUser_whenUserAlreadyExists() throws Exception {
        when(userService.createUser(Mockito.any(User.class)))
                .thenThrow(new DataAlreadyExistsException("Usuário 'john_doe' já está cadastrado."));

        mockMvc.perform(post(apiBaseUrl + "/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Usuário 'john_doe' já está cadastrado."));

        Mockito.verify(userService, times(1)).createUser(Mockito.any(User.class));
    }
}
