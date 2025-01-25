package com.app.us_twogether.domain.user;

import com.app.us_twogether.exception.DataAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserAutomationTest {
    @Autowired
    private UserService userService;

    @Test
    public void testCreateUser(){
        // Criar e salvar um User que referencia o NotificationUser padrão
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        userService.saveUser(user);

        // Busca o usuário cadastrado
        User savedUser = (userService.findByUsername("john_doe"));

        // Validações
        assertThat(savedUser.getUsername()).isEqualTo("john_doe");
    }

    @Test
    public void testCreateDuplicateUser(){
        // Criar e salvar um User que referencia o NotificationUser padrão
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        userService.saveUser(user);

        // Testar se o sistema lança uma exceção ao criar outro usuário com o mesmo username
        assertThrows(DataAlreadyExistsException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testUpdateUser(){
        // Criar e salvar um User que referencia o NotificationUser padrão
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");

        // Salvo o usuário
        userService.saveUser(user);

        // Busca o usuário cadastrado
        User savedUser = (userService.findByUsername("john_doe"));

        // Crio um usuário para alterar os dados
        User updatedUser = new User("john_doe", "4321", "Roger", "roger@example.com", "11945345678", "US");

        userService.updateUser("john_doe", updatedUser);

        // Validações
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Roger");
        assertThat(savedUser.getEmail()).isEqualTo("roger@example.com");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("11945345678");
    }

    @Test
    public void testPasswordEncoderUser(){
        // Criar e salvar um User que referencia o NotificationUser padrão
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");

        // Salvo o usuário
        userService.saveUser(user);

        // Busca o usuário cadastrado
        User savedUser = (userService.findByUsername("john_doe"));

        // Validações
        assertThat(savedUser).isNotNull();
        assertNotSame(savedUser.getPhoneNumber(),"1234");
    }
}
