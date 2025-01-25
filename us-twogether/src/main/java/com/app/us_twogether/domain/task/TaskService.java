package com.app.us_twogether.domain.task;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.category.CategoryService;
import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.category.subCategory.SubCategoryService;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.domain.user.UserSpaceRoleRepository;
import com.app.us_twogether.domain.space.SpaceService;
import com.app.us_twogether.domain.user.UserService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private UserSpaceRoleRepository userSpaceRoleRepository;

    public TaskResponseDTO createTask(String usernameCreation, Long spaceId, TaskRequestDTO task) {
        Space space = spaceService.findSpaceById(spaceId);
        User user = userService.findByUsername(usernameCreation);

        User userResponsible = userService.findByUsername(task.userResponsible());
        validateUserAndSpace(userResponsible, space);

        Category category = categoryService.getCategory(task.categoryId());
        SubCategory subCategory = subCategoryService.getSubCategory(task.subCategoryId());

        Task newTask = new Task();
        newTask.setSpace(space);
        newTask.setUserCreation(user);
        newTask.setUserResponsible(userResponsible);
        newTask.setCategory(category);
        newTask.setSubCategory(subCategory);
        newTask.setTitle(task.title());
        newTask.setDescription(task.description());
        newTask.setDateCreation(LocalDate.now());
        newTask.setTimeCreation(LocalTime.now());
        newTask.setDateCompletion(task.dateCompletion());
        newTask.setTimeCompletion(task.timeCompletion());
        newTask.setAttachment(task.attachment());
        newTask.setCompleted(false);

        taskRepository.save(newTask);

        return taskMapper.toResponseDTO(newTask);
    }

    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO task) {
        Task updatedTask = findTaskById(taskId);

        if (!updatedTask.getUserResponsible().getUsername().equals(task.userResponsible())) {
            User userResponsible = userService.findByUsername(task.userResponsible());
            validateUserAndSpace(userResponsible, updatedTask.getSpace());
            updatedTask.setUserResponsible(userResponsible);
        }

        Category category = categoryService.getCategory(task.categoryId());
        SubCategory subCategory = subCategoryService.getSubCategory(task.subCategoryId());

        updatedTask.setCategory(category);
        updatedTask.setSubCategory(subCategory);
        updatedTask.setTitle(task.title());
        updatedTask.setDescription(task.description());
        updatedTask.setDateCompletion(task.dateCompletion());
        updatedTask.setTimeCompletion(task.timeCompletion());
        updatedTask.setAttachment(task.attachment());
        updatedTask.setDateEnd(task.dateEnd());
        updatedTask.setTimeEnd(task.timeEnd());
        updatedTask.setCompleted(task.completed());

        taskRepository.save(updatedTask);

        return taskMapper.toResponseDTO(updatedTask);
    }

    public void deletedTask(Long taskId) {
        Task task = findTaskById(taskId);

        taskRepository.delete(task);
    }

    public TaskResponseDTO getTask(Long taskId) {
        Task existingTask = findTaskById(taskId);

        return taskMapper.toResponseDTO(existingTask);
    }

    public List<TaskResponseDTO> getAllTaskFromSpace(Long spaceId) {
        return taskRepository.findBySpace(spaceId);
    }

    public List<TaskResponseDTO> getAllTaskFromSpaceAndDate(Long spaceId, LocalDate dateCompletion) {
        return taskRepository.findBySpaceAndDate(spaceId, dateCompletion);
    }

    public TaskResponseDTO completedTask(Long taskId) {
        Task task = findTaskById(taskId);

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        task.setDateEnd(localDate);
        task.setTimeEnd(localTime);
        task.setCompleted(true);

        taskRepository.save(task);

        return taskMapper.toResponseDTO(task);
    }

    private void validateUserAndSpace(User user, Space space) {
        if (!userSpaceRoleRepository.existsByUserAndSpace(user, space)) {
            throw new DataAlreadyExistsException("Usuário responsável não possui acesso a esse espaço");
        }
    }

    private Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task não encontrada"));
    }

}
