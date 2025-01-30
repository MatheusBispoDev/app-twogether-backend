package com.app.us_twogether.domain.reminder;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.category.CategoryService;
import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.category.subCategory.SubCategoryService;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.space.SpaceService;
import com.app.us_twogether.domain.user.UserService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderMapper reminderMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    public ReminderResponseDTO createReminder(String usernameCreation, Long spaceId, ReminderRequestDTO reminder) {
        Space space = spaceService.findSpaceById(spaceId);
        User user = userService.getUserByUsername(usernameCreation);

        Category category = categoryService.getCategory(reminder.categoryId());
        SubCategory subCategory = subCategoryService.getSubCategory(reminder.subCategoryId());

        Reminder newReminder = new Reminder();
        newReminder.setSpace(space);
        newReminder.setUserCreation(user);
        newReminder.setCategory(category);
        newReminder.setSubCategory(subCategory);
        newReminder.setTitle(reminder.title());
        newReminder.setDescription(reminder.description());
        newReminder.setDateCreation(LocalDate.now());
        newReminder.setTimeCreation(LocalTime.now());
        newReminder.setDateCompletion(reminder.dateCompletion());
        newReminder.setTimeCompletion(reminder.timeCompletion());
        newReminder.setCompleted(false);

        reminderRepository.save(newReminder);

        return reminderMapper.toResponseDTO(newReminder);
    }

    public ReminderResponseDTO updateReminder(Long remindersId, ReminderRequestDTO reminder) {
        Reminder updatedReminder = findReminderById(remindersId);

        Category category = categoryService.getCategory(reminder.categoryId());
        SubCategory subCategory = subCategoryService.getSubCategory(reminder.subCategoryId());

        updatedReminder.setCategory(category);
        updatedReminder.setSubCategory(subCategory);
        updatedReminder.setTitle(reminder.title());
        updatedReminder.setDescription(reminder.description());
        updatedReminder.setDateCompletion(reminder.dateCompletion());
        updatedReminder.setTimeCompletion(reminder.timeCompletion());
        updatedReminder.setCompleted(reminder.completed());

        reminderRepository.save(updatedReminder);

        return reminderMapper.toResponseDTO(updatedReminder);
    }

    public void deletedReminder(Long remindersId) {
        Reminder reminder = findReminderById(remindersId);

        reminderRepository.delete(reminder);
    }

    public ReminderResponseDTO getReminder(Long remindersId) {
        Reminder reminder = findReminderById(remindersId);

        return reminderMapper.toResponseDTO(reminder);
    }

    public List<ReminderResponseDTO> getAllRemindersFromSpace(Long spaceId, LocalDate dateCompletion) {
        return reminderRepository.findBySpaceAndDate(spaceId, dateCompletion);
    }

    public ReminderResponseDTO completedReminder(Long remindersId) {
        //TODO Adicionar agendamento de completed e retirar requisição do controlller
        Reminder reminder = findReminderById(remindersId);
        LocalDateTime completion = reminder.getDateCompletion().atTime(reminder.getTimeCompletion());

        if (completion.isBefore(LocalDateTime.now())) {
            reminder.setCompleted(true);
            reminderRepository.save(reminder);
        }
        return reminderMapper.toResponseDTO(reminder);
    }

    private Reminder findReminderById(Long remindersId) {
        return reminderRepository.findById(remindersId).orElseThrow(() -> new ResourceNotFoundException("Lembrete não encontrada"));
    }

}
