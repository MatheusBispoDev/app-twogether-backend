package com.app.us_twogether.domain.task;

import com.app.us_twogether.domain.category.*;
import com.app.us_twogether.domain.category.subCategory.SubCategoryRequestDTO;
import com.app.us_twogether.domain.category.subCategory.SubCategoryService;
import com.app.us_twogether.domain.space.SpaceService;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.*;
import java.util.List;

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

    private LocalDate fixedDate;

    private LocalTime fixedTime;

    private TaskRequestDTO taskDTO;

    @BeforeEach
    public void setup(){
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        userService.createUser(user);

        spaceId = spaceService.createSpace(user).spaceId();

        categoryId = categoryService.createCategory(spaceId,
                new CategoryRequestDTO("Home", "#FF5733", CategoryType.ACTIVITY)).categoryId();

        subCategoryId = subCategoryService.createSubCategory(categoryId,
                new SubCategoryRequestDTO("Home", "#FF5733")).subCategoryId();

        Instant fixedInstant = Instant.parse("2024-02-12T15:30:00Z");
        fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));

        fixedDate = LocalDate.now(fixedClock);
        fixedTime = LocalTime.now(fixedClock);

        taskDTO = new TaskRequestDTO("john_doe", categoryId, subCategoryId, "Colocar o lixo para fora",
                "Colocar o lixo para fora antes das 10h", "O saco preto está embaixo da pia",
                fixedDate, fixedTime, null, null, "", false);
    }

    @Test
    public void shouldCreateTask_whenCredentialsAreValid() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long taskId = taskService.createTask("john_doe", spaceId, taskDTO).taskId();

        TaskResponseDTO task = taskService.getTask(taskId);

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

    @Test
    public void shouldUpdateTask_whenCompletedIsFalse() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long createId = taskService.createTask("john_doe", spaceId, taskDTO).taskId();

        TaskResponseDTO updatedTask = updateTask(createId, false);

        TaskResponseDTO task = taskService.getTask(updatedTask.taskId());

        assertNotNull(task);
        assertNotNull(task.taskId());
        assertEquals("john_doe", task.userCreation());
        assertEquals("john_doe", task.userResponsible());
        assertEquals(updatedTask.categoryId(), task.categoryId());
        assertEquals(updatedTask.subCategoryId(), task.subCategoryId());
        assertEquals("Comprar Fone com Microfone", task.title());
        assertEquals("Comprar Fone com Microfone", task.description());
        assertEquals("O fone quebrou, precisa comprar um novo", task.observation());
        assertEquals(fixedDate, task.dateCompletion());
        assertEquals(fixedTime, task.timeCompletion());
        assertNull(task.dateEnd());
        assertNull(task.timeEnd());
        assertEquals("", task.attachment());
        assertFalse(task.completed());
    }

    @Test
    public void shouldUpdateTask_whenCompletedIsTrue() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long createId = taskService.createTask("john_doe", spaceId, taskDTO).taskId();

        TaskResponseDTO updatedTask = updateTask(createId, true);

        TaskResponseDTO task = taskService.getTask(updatedTask.taskId());

        assertNotNull(task);
        assertNotNull(task.taskId());
        assertEquals("john_doe", task.userCreation());
        assertEquals("john_doe", task.userResponsible());
        assertEquals(updatedTask.categoryId(), task.categoryId());
        assertEquals(updatedTask.subCategoryId(), task.subCategoryId());
        assertEquals("Comprar Fone com Microfone", task.title());
        assertEquals("Comprar Fone com Microfone", task.description());
        assertEquals("O fone quebrou, precisa comprar um novo", task.observation());
        assertEquals(fixedDate, task.dateCompletion());
        assertEquals(fixedTime, task.timeCompletion());
        assertEquals(fixedDate, task.dateEnd());
        assertEquals(fixedTime, task.timeEnd());
        assertEquals("", task.attachment());
        assertTrue(task.completed());
    }

    @Test
    public void shouldCompetedTask_whenIsIncomplete() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long createId = taskService.createTask("john_doe", spaceId, taskDTO).taskId();

        taskService.completedTask(createId);

        TaskResponseDTO task = taskService.getTask(createId);

        assertNotNull(task);
        assertNotNull(task.taskId());
        assertEquals(LocalDate.now(), task.dateEnd());
        assertTrue(task.completed());
    }

    @Test
    public void shouldCompetedTask_whenIsAlreadyCompleted() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long createId = taskService.createTask("john_doe", spaceId, taskDTO).taskId();

        taskService.completedTask(createId);
        taskService.completedTask(createId);

        TaskResponseDTO task = taskService.getTask(createId);

        assertNotNull(task);
        assertNotNull(task.taskId());
        assertEquals(LocalDate.now(), task.dateEnd());
        assertTrue(task.completed());
    }

    @Test
    public void shouldDeleteTask_whenCredentialsAreValid(){
        TaskResponseDTO task = taskService.createTask("john_doe", spaceId, taskDTO);

        taskService.deletedTask(task.taskId());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTask(task.categoryId()));
    }

    @Test
    public void shouldGetAllTaskFromSpace(){
        List<TaskRequestDTO> categoriesToCreate = List.of(
                taskDTO,
                taskDTO,
                taskDTO,
                taskDTO
        );

        categoriesToCreate.forEach(task -> taskService.createTask("john_doe", spaceId, task));

        List<TaskResponseDTO> task = taskService.getAllTaskFromSpace(spaceId);

        assertNotNull(task, "A lista de tarefas não deveria ser nula");
        assertEquals(4, task.size(), "O número de tarefas deveria ser 4");

        List<String> actualTitles = task.stream()
                .map(TaskResponseDTO::title)
                .toList();

        List<String> expectedTitles = List.of(taskDTO.title(), taskDTO.title(), taskDTO.title(), taskDTO.title());

        assertTrue(actualTitles.containsAll(expectedTitles), "A lista de tarefas deve conter os títulos esperados");
    }

    @Test
    public void shouldGetAllTaskFromSpaceAndDate(){
        String title = "Colocar o lixo para fora";

        List<TaskRequestDTO> categoriesToCreate = List.of(
                new TaskRequestDTO("john_doe", categoryId, subCategoryId, title,
                        "Colocar o lixo para fora antes das 10h", "O saco preto está embaixo da pia",
                        LocalDate.now(), LocalTime.now(), null, null, "", false),
                new TaskRequestDTO("john_doe", categoryId, subCategoryId, title,
                "Colocar o lixo para fora antes das 10h", "O saco preto está embaixo da pia",
                        LocalDate.now(), LocalTime.now(), null, null, "", false)
        );

        categoriesToCreate.forEach(task -> taskService.createTask("john_doe", spaceId, task));

        List<TaskResponseDTO> task = taskService.getAllTaskFromSpaceAndDate(spaceId, LocalDate.now());

        assertNotNull(task, "A lista de tarefas não deveria ser nula");
        assertEquals(2, task.size(), "O número de tarefas deveria ser 2");

        List<String> actualTitles = task.stream()
                .map(TaskResponseDTO::title)
                .toList();

        List<String> expectedTitles = List.of(title, title);

        assertTrue(actualTitles.containsAll(expectedTitles), "A lista de tarefas deve conter os títulos esperados");
    }

    private TaskResponseDTO updateTask(Long taskId, boolean completed){
        Long workCategoryId = categoryService.createCategory(spaceId,
                new CategoryRequestDTO("Work", "#FF5733", CategoryType.ACTIVITY)).categoryId();

        Long workSubCategoryId = subCategoryService.createSubCategory(workCategoryId,
                new SubCategoryRequestDTO("Equipments", "#FF5733")).subCategoryId();

        TaskRequestDTO updatedTask = new TaskRequestDTO("john_doe", workCategoryId, workSubCategoryId, "Comprar Fone com Microfone",
                "Comprar Fone com Microfone", "O fone quebrou, precisa comprar um novo",
                fixedDate, fixedTime, fixedDate, fixedTime, "", completed);

        return taskService.updateTask(taskId, updatedTask);
    }

}
