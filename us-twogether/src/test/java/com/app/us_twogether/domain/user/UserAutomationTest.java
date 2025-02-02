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
    public void shouldCreateUser_whenCredentialsAreValid(){
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        userService.createUser(user);

        User savedUser = (userService.getUserByUsername("john_doe"));

        assertThat(savedUser.getUsername()).isEqualTo("john_doe");
    }

    @Test
    public void shouldCreateUser_whenUserAlreadyExists(){
        // Criar e salvar um User que referencia o NotificationUser padrão
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        userService.createUser(user);

        // Testar se o sistema lança uma exceção ao criar outro usuário com o mesmo username
        assertThrows(DataAlreadyExistsException.class, () -> userService.createUser(user));
    }

    @Test
    public void shouldUpdateUser_whenCredentialsAreValid(){
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");

        userService.createUser(user);

        UserRequestDTO updatedUser = new UserRequestDTO("john_doe", "4321", "Roger", "roger@example.com", "11945345678", "TST");

        userService.updateUser("john_doe", updatedUser);

        User savedUser = (userService.getUserByUsername("john_doe"));

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isNotEqualTo("4321");
        assertThat(savedUser.getName()).isEqualTo("Roger");
        assertThat(savedUser.getEmail()).isEqualTo("roger@example.com");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("11945345678");
        assertThat(savedUser.getType()).isEqualTo("TST");
    }

    @Test
    public void shouldGetPasswordUser_whenPasswordEncoder(){
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");

        userService.createUser(user);

        User savedUser = (userService.getUserByUsername("john_doe"));

        assertThat(savedUser).isNotNull();
        assertNotSame(savedUser.getPassword(),"1234");
    }
}
