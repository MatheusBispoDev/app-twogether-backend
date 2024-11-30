package com.app.us_twogether.user;

import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.security.TokenService;
import com.app.us_twogether.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void testLoginUser() throws Exception {
        // Criar e salvar um User que referencia o NotificationUser padrão
        String username = "john_doe";
        String password = "1234";

        // Criação de usuário
        User userDetails = new User(username, password, "John Doe", "john@example.com", "11932178425", "US");
        userService.saveUser(userDetails);

        // Validação de autenticação de usuário
        var usernamePassword = new UsernamePasswordAuthenticationToken(username, password);
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void testRegisterUser() throws Exception {
        // JSON de entrada
        String registerRequest = """
        {
            "username": "john_doe",
            "password": "1234",
            "name": "John Doe",
            "email": "john@example.com",
            "phoneNumber": "11932178425",
            "type": "US"
        }
        """;

        // Execução da requisição e verificação da resposta
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isOk());
    }
}
