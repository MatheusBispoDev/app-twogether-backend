package com.app.us_twogether.domain.user;

import com.app.us_twogether.domain.authentication.token.TokenService;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Transactional
public class AuthenticationTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    private final UserRequestDTO userRequestDTO = new UserRequestDTO("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");

    private final UserResponseDTO userResponseDTO = new UserResponseDTO("john_doe", "John Doe", "john@example.com", "11932178425", "US");

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testLoginUser() throws Exception {
        // Criar e salvar um User que referencia o NotificationUser padrão
        String username = "john_doe";
        String password = "1234";

        // Criação de usuário
        User userDetails = new User(username, password, "John Doe", "john@example.com", "11932178425", "US");
        userService.createUser(userDetails);

        // Validação de autenticação de usuário
        var usernamePassword = new UsernamePasswordAuthenticationToken(username, password);
        var auth = this.authenticationManager.authenticate(usernamePassword);

        //var token = tokenService.generateToken((User) auth.getPrincipal());

        // Faz o JSON para simular a requisição de Login
        // JSON de entrada
        String loginRequest = """
        {
            "username": "john_doe",
            "password": "1234"
        }
        """;

        // Execução da requisição e verificação da resposta
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserSuccessfully() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post(apiBaseUrl + "/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("11932178425"))
                .andExpect(jsonPath("$.type").value("US"));

        Mockito.verify(userService, times(1)).createUser(Mockito.any(User.class));
    }

    @Test
    public void testErrorWhenUserAlreadyExists() throws Exception {
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
