package com.app.us_twogether.service;

import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.space.UserSpaceRole;
import com.app.us_twogether.domain.task.Task;
import com.app.us_twogether.domain.task.TaskDTO;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.repository.TaskRepository;
import com.app.us_twogether.repository.UserSpaceRoleRepository;
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
    TaskRepository taskRepository;

    @Autowired
    private UserSpaceRoleRepository userSpaceRoleRepository;

    public TaskDTO createTask(String usernameCreation, Long spaceId, TaskDTO taskDTO) {
        Space space = spaceService.findSpaceById(spaceId);
        User user = userService.findByUsername(usernameCreation);

        User userResponsible = userService.findByUsername(validateUsernameResponsible(usernameCreation, taskDTO.userResponsible()));
        validateUserAndSpace(userResponsible, space);

        Task newTask = new Task();
        newTask.setSpaceId(space);
        newTask.setUserCreation(user);
        newTask.setUserResponsible(userResponsible);
        newTask.setTitle(taskDTO.title());
        newTask.setDescription(taskDTO.description());
        newTask.setDateCreation(taskDTO.dateCreation());
        newTask.setTimeCreation(taskDTO.timeCreation());
        newTask.setDateCompletion(taskDTO.dateCompletion());
        newTask.setTimeCompletion(taskDTO.timeCompletion());
        newTask.setAttachment(taskDTO.attachment());
        newTask.setCompleted(false);

        taskRepository.save(newTask);

        return castTaskToDTO(newTask);
    }

    public TaskDTO updateTask(Long taskId, TaskDTO updatedTask) {
        Task existingTask = findTaskById(taskId);

        if (!existingTask.getUserResponsible().getUsername().equals(updatedTask.userResponsible())) {
            User userResponsible = userService.findByUsername(updatedTask.userResponsible());
            validateUserAndSpace(userResponsible, existingTask.getSpaceId());
            existingTask.setUserResponsible(userResponsible);
        }

        existingTask.setTitle(updatedTask.title());
        existingTask.setDescription(updatedTask.description());
        existingTask.setDateCompletion(updatedTask.dateCompletion());
        existingTask.setTimeCompletion(updatedTask.timeCompletion());
        existingTask.setAttachment(updatedTask.attachment());
        existingTask.setDateEnd(updatedTask.dateEnd());
        existingTask.setTimeEnd(updatedTask.timeEnd());
        existingTask.setCompleted(updatedTask.completed());

        taskRepository.save(existingTask);

        return castTaskToDTO(existingTask);
    }

    public void deletedTask(Long taskId) {
        Task existingTask = findTaskById(taskId);

        taskRepository.delete(existingTask);
    }

    public TaskDTO getTask(Long taskId) {
        Task existingTask = findTaskById(taskId);

        return castTaskToDTO(existingTask);
    }

    public List<TaskDTO> getAllTaskFromSpace(Long spaceId, LocalDate dateCompletion) {
        Space space = spaceService.findSpaceById(spaceId);
        return taskRepository.findBySpaceAndDate(space, dateCompletion);
    }

    public TaskDTO completedTask(Long taskId) {
        Task existingTask = findTaskById(taskId);

        boolean isCompleted = true;
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        existingTask.setDateEnd(localDate);
        existingTask.setTimeEnd(localTime);
        existingTask.setCompleted(isCompleted);

        taskRepository.save(existingTask);

        return castTaskToDTO(existingTask);
    }

    private TaskDTO castTaskToDTO(Task task) {
        return new TaskDTO(task.getTaskId(), task.getUserCreation().getUsername(), task.getUserResponsible().getUsername(),
                task.getTitle(), task.getDescription(), task.getObservation(), task.getDateCreation(),
                task.getTimeCreation(), task.getDateCompletion(), task.getTimeCompletion(),
                task.getDateEnd(), task.getTimeEnd(), task.getObservation(), task.isCompleted());
    }

    private void validateUserAndSpace(User user, Space space) {
        //TODO Implementar validação de acesso no SpaceAccessInterceptor
        if (!userSpaceRoleRepository.existsByUserAndSpace(user, space)) {
            //TODO melhorar excecao
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
