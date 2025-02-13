package com.app.us_twogether.domain.task;

import com.app.us_twogether.domain.category.*;
import com.app.us_twogether.domain.category.subCategory.SubCategoryRequestDTO;
import com.app.us_twogether.domain.category.subCategory.SubCategoryService;
import com.app.us_twogether.domain.space.SpaceService;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class TaskTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private TaskService taskService;

    private Long spaceId;

    private Long categoryId;

    private Long subCategoryId;

    @MockBean
    private Clock clock;

    private Clock fixedClock;

    @BeforeEach
    public void setup(){
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        userService.createUser(user);

        spaceId = spaceService.createSpace(user).spaceId();

        categoryId = categoryService.createCategory(spaceId,
                new CategoryRequestDTO("Home", "#FF5733", CategoryType.EXPENSE)).categoryId();

        subCategoryId = subCategoryService.createSubCategory(categoryId,
                new SubCategoryRequestDTO("Home", "#FF5733")).subCategoryId();

        Instant fixedInstant = Instant.parse("2024-02-12T15:30:00Z");
        fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));
    }

    @Test
    public void shouldCreateTask_whenCredentialsAreValid() {
        LocalDate fixedDate = LocalDate.now(fixedClock);
        LocalTime fixedTime = LocalTime.now(fixedClock);

        TaskRequestDTO taskDTO = new TaskRequestDTO("john_doe", categoryId, subCategoryId, "Colocar o lixo para fora",
                "Colocar o lixo para fora antes das 10h", "O saco preto está embaixo da pia",
                fixedDate, fixedTime, fixedDate, fixedTime, "", false);

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        TaskResponseDTO response = taskService.createTask("john_doe", spaceId, taskDTO);

        TaskResponseDTO task = taskService.getTask(response.taskId());

        assertNotNull(task);
        assertNotNull(task.taskId());
        assertEquals("john_doe", task.userCreation());
        assertEquals("john_doe", task.userResponsible());
        assertEquals(categoryId, task.categoryId());
        assertEquals(subCategoryId, task.subCategoryId());
        assertEquals("Colocar o lixo para fora", task.title());
        assertEquals("Colocar o lixo para fora antes das 10h", task.description());
        assertEquals("O saco preto está embaixo da pia", task.observation());
        assertEquals(fixedDate, task.dateCompletion());
        assertEquals(fixedTime, task.timeCompletion());
        assertNull(task.dateEnd());
        assertNull(task.timeEnd());
        assertEquals("", task.attachment());
        assertFalse(task.completed());
    }
}
