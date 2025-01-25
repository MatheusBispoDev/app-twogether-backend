package com.app.us_twogether.domain.task;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.category.CategoryService;
import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.category.subCategory.SubCategoryService;
import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.space.UserSpaceRole;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.repository.UserSpaceRoleRepository;
import com.app.us_twogether.service.SpaceService;
import com.app.us_twogether.service.UserService;
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


    public TaskResponseDTO createTask(String usernameCreation, Long spaceId, TaskRequestDTO taskDTO) {
        Space space = spaceService.findSpaceById(spaceId);
        User user = userService.findByUsername(usernameCreation);

        User userResponsible = userService.findByUsername(validateUsernameResponsible(usernameCreation, taskDTO.userResponsible()));
        validateUserAndSpace(userResponsible, space);

        Category category = categoryService.getCategory(taskDTO.categoryId());
        SubCategory subCategory = subCategoryService.getSubCategory(taskDTO.subCategoryId());

        Task newTask = new Task();
        newTask.setSpace(space);
        newTask.setUserCreation(user);
        newTask.setUserResponsible(userResponsible);
        newTask.setCategory(category);
        newTask.setSubCategory(subCategory);
        newTask.setTitle(taskDTO.title());
        newTask.setDescription(taskDTO.description());
        newTask.setDateCreation(LocalDate.now());
        newTask.setTimeCreation(LocalTime.now());
        newTask.setDateCompletion(taskDTO.dateCompletion());
        newTask.setTimeCompletion(taskDTO.timeCompletion());
        newTask.setAttachment(taskDTO.attachment());
        newTask.setCompleted(false);

        taskRepository.save(newTask);

        return taskMapper.toResponseDTO(newTask);
    }

    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO updatedTask) {
        Task existingTask = findTaskById(taskId);

        if (!existingTask.getUserResponsible().getUsername().equals(updatedTask.userResponsible())) {
            User userResponsible = userService.findByUsername(updatedTask.userResponsible());
            validateUserAndSpace(userResponsible, existingTask.getSpace());
            existingTask.setUserResponsible(userResponsible);
        }

        Category category = categoryService.getCategory(updatedTask.categoryId());
        SubCategory subCategory = subCategoryService.getSubCategory(updatedTask.subCategoryId());

        existingTask.setCategory(category);
        existingTask.setSubCategory(subCategory);
        existingTask.setTitle(updatedTask.title());
        existingTask.setDescription(updatedTask.description());
        existingTask.setDateCompletion(updatedTask.dateCompletion());
        existingTask.setTimeCompletion(updatedTask.timeCompletion());
        existingTask.setAttachment(updatedTask.attachment());
        existingTask.setDateEnd(updatedTask.dateEnd());
        existingTask.setTimeEnd(updatedTask.timeEnd());
        existingTask.setCompleted(updatedTask.completed());

        taskRepository.save(existingTask);

        return taskMapper.toResponseDTO(existingTask);
    }

    public void deletedTask(Long taskId) {
        Task existingTask = findTaskById(taskId);

        taskRepository.delete(existingTask);
    }

    public TaskResponseDTO getTask(Long taskId) {
        Task existingTask = findTaskById(taskId);

        return taskMapper.toResponseDTO(existingTask);
    }

    public List<TaskResponseDTO> getAllTaskFromSpace(Long spaceId, LocalDate dateCompletion) {
        Space space = spaceService.findSpaceById(spaceId);
        return taskRepository.findBySpaceAndDate(space, dateCompletion);
    }

    public TaskResponseDTO completedTask(Long taskId) {
        Task existingTask = findTaskById(taskId);

        boolean isCompleted = true;
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        existingTask.setDateEnd(localDate);
        existingTask.setTimeEnd(localTime);
        existingTask.setCompleted(isCompleted);

        taskRepository.save(existingTask);

        return taskMapper.toResponseDTO(existingTask);
    }

    private void validateUserAndSpace(User user, Space space) {
        //TODO Implementar validação de acesso no SpaceAccessInterceptor
        if (!userSpaceRoleRepository.existsByUserAndSpace(user, space)) {
            throw new DataAlreadyExistsException("Usuário responsável não possui acesso a esse espaço");
        }
    }

    private void validateAccessLevelUser(User user, Space space) {
        //TODO Implementar nivel de acesso
        UserSpaceRole userSpaceRole = spaceService.findUserRoleByUserAndSpace(user, space);

        if (userSpaceRole.getAccessLevel() == AccessLevel.INVITED) {
            //TODO Melhorar Excecao e Validação de nível de acesso
            throw new DataAlreadyExistsException("Usuário sem acesso a tal funcionalidade");
        }
    }

    private Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task não encontrada"));
    }

    private String validateUsernameResponsible(String usernameCreation, String usernameResponsible) {
        if (!usernameCreation.equals(usernameResponsible)) {
            return usernameResponsible;
        }
        return usernameCreation;
    }

}
