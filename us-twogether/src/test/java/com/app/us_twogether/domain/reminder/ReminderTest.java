package com.app.us_twogether.domain.reminder;

import com.app.us_twogether.domain.category.CategoryRequestDTO;
import com.app.us_twogether.domain.category.CategoryService;
import com.app.us_twogether.domain.category.CategoryType;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class ReminderTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ReminderService reminderService;

    private Long spaceId;

    private Long categoryId;

    private Long subCategoryId;

    @MockBean
    private Clock clock;

    private Clock fixedClock;

    private LocalDate fixedDate;

    private LocalTime fixedTime;

    private ReminderRequestDTO reminderDTO;

    @BeforeEach
    public void setup() {
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

        reminderDTO = new ReminderRequestDTO(categoryId, subCategoryId, "Colocar o lixo para fora",
                "Colocar o lixo para fora antes das 10h", fixedDate, fixedTime, false);
    }

    @Test
    public void shouldCreateReminder_whenCredentialsAreValid() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long reminderId = reminderService.createReminder("john_doe", spaceId, reminderDTO).reminderId();

        ReminderResponseDTO reminder = reminderService.getReminder(reminderId);

        assertNotNull(reminder);
        assertNotNull(reminder.reminderId());
        assertEquals("john_doe", reminder.userCreation());
        assertEquals(categoryId, reminder.categoryId());
        assertEquals(subCategoryId, reminder.subCategoryId());
        assertEquals("Colocar o lixo para fora", reminder.title());
        assertEquals("Colocar o lixo para fora antes das 10h", reminder.description());
        assertEquals(fixedDate, reminder.dateCompletion());
        assertEquals(fixedTime, reminder.timeCompletion());
        assertFalse(reminder.completed());
    }

    @Test
    public void shouldUpdateReminder_whenCompletedIsFalse() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long createId = reminderService.createReminder("john_doe", spaceId, reminderDTO).reminderId();

        ReminderResponseDTO updatedReminder = updateReminder(createId, false);

        ReminderResponseDTO reminder = reminderService.getReminder(updatedReminder.reminderId());

        assertNotNull(reminder);
        assertNotNull(reminder.reminderId());
        assertEquals("john_doe", reminder.userCreation());
        assertEquals(updatedReminder.categoryId(), reminder.categoryId());
        assertEquals(updatedReminder.subCategoryId(), reminder.subCategoryId());
        assertEquals("Comprar Fone com Microfone", reminder.title());
        assertEquals("Comprar Fone com Microfone", reminder.description());
        assertEquals(fixedDate, reminder.dateCompletion());
        assertEquals(fixedTime, reminder.timeCompletion());
        assertFalse(reminder.completed());
    }

    @Test
    public void shouldUpdateReminder_whenCompletedIsTrue() {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long createId = reminderService.createReminder("john_doe", spaceId, reminderDTO).reminderId();

        ReminderResponseDTO updatedReminder = updateReminder(createId, true);

        ReminderResponseDTO reminder = reminderService.getReminder(updatedReminder.reminderId());

        assertNotNull(reminder);
        assertNotNull(reminder.reminderId());
        assertEquals("john_doe", reminder.userCreation());
        assertEquals(updatedReminder.categoryId(), reminder.categoryId());
        assertEquals(updatedReminder.subCategoryId(), reminder.subCategoryId());
        assertEquals("Comprar Fone com Microfone", reminder.title());
        assertEquals("Comprar Fone com Microfone", reminder.description());
        assertEquals(fixedDate, reminder.dateCompletion());
        assertEquals(fixedTime, reminder.timeCompletion());
        assertTrue(reminder.completed());
    }

    @Test
    public void shouldCompetedReminder_whenIsIncomplete() {
        Long createId = reminderService.createReminder("john_doe", spaceId, reminderDTO).reminderId();

        reminderService.completedReminder(createId);

        ReminderResponseDTO reminder = reminderService.getReminder(createId);

        assertNotNull(reminder);
        assertNotNull(reminder.reminderId());
        assertTrue(reminder.completed());
    }

    @Test
    public void shouldCompetedReminder_whenIsAlreadyCompleted() {
        Long createId = reminderService.createReminder("john_doe", spaceId, reminderDTO).reminderId();

        reminderService.completedReminder(createId);
        reminderService.completedReminder(createId);

        ReminderResponseDTO reminder = reminderService.getReminder(createId);

        assertNotNull(reminder);
        assertNotNull(reminder.reminderId());
        assertTrue(reminder.completed());
    }

    @Test
    public void shouldDeleteReminder_whenCredentialsAreValid(){
        ReminderResponseDTO reminderResponse = reminderService.createReminder("john_doe", spaceId, reminderDTO);

        reminderService.deletedReminder(reminderResponse.reminderId());

        assertThrows(ResourceNotFoundException.class, () -> reminderService.getReminder(reminderResponse.reminderId()));
    }

    @Test
    public void shouldGetAllReminderFromSpace() {
        List<ReminderRequestDTO> remindersToCreate = List.of(
                reminderDTO,
                reminderDTO,
                reminderDTO,
                reminderDTO
        );

        remindersToCreate.forEach(reminder -> reminderService.createReminder("john_doe", spaceId, reminder));

        List<ReminderResponseDTO> reminder = reminderService.getAllRemindersFromSpace(spaceId);

        assertNotNull(reminder, "A lista de tarefas não deveria ser nula");
        assertEquals(4, reminder.size(), "O número de tarefas deveria ser 4");

        List<String> actualTitles = reminder.stream()
                .map(ReminderResponseDTO::title)
                .toList();

        List<String> expectedTitles = List.of(reminderDTO.title(), reminderDTO.title(), reminderDTO.title(), reminderDTO.title());

        assertTrue(actualTitles.containsAll(expectedTitles), "A lista de tarefas deve conter os títulos esperados");
    }

    @Test
    public void shouldGetAllReminderFromSpaceAndDate() {
        String title = "Colocar o lixo para fora";

        List<ReminderRequestDTO> remindersToCreate = List.of(
                new ReminderRequestDTO(categoryId, subCategoryId,
                        "Colocar o lixo para fora antes das 10h", "O saco preto está embaixo da pia",
                        LocalDate.now(), LocalTime.now(), false),
                new ReminderRequestDTO(categoryId, subCategoryId, title,
                        "Colocar o lixo para fora antes das 10h",
                        LocalDate.now(), LocalTime.now(), false)
        );

        remindersToCreate.forEach(reminder -> reminderService.createReminder("john_doe", spaceId, reminder));

        List<ReminderResponseDTO> reminder = reminderService.getAllRemindersFromSpaceAndDate(spaceId, LocalDate.now());

        assertNotNull(reminder, "A lista de tarefas não deveria ser nula");
        assertEquals(2, reminder.size(), "O número de tarefas deveria ser 2");

        List<String> actualTitles = reminder.stream()
                .map(ReminderResponseDTO::title)
                .toList();

        List<String> expectedTitles = List.of(title, title);

        assertTrue(actualTitles.containsAll(expectedTitles), "A lista de tarefas deve conter os títulos esperados");
    }

    public ReminderResponseDTO updateReminder(Long reminderId, boolean completed) {
        Long workCategoryId = categoryService.createCategory(spaceId,
                new CategoryRequestDTO("Work", "#FF5733", CategoryType.ACTIVITY)).categoryId();

        Long workSubCategoryId = subCategoryService.createSubCategory(workCategoryId,
                new SubCategoryRequestDTO("Equipments", "#FF5733")).subCategoryId();

        ReminderRequestDTO updatedReminder = new ReminderRequestDTO(workCategoryId, workSubCategoryId, "Comprar Fone com Microfone",
                "Comprar Fone com Microfone", fixedDate, fixedTime, completed);

        return reminderService.updateReminder(reminderId, updatedReminder);
    }

}
