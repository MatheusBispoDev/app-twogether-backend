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

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private UserSpaceRoleRepository userSpaceRoleRepository;

    public void createTask(User user, Space space, TaskDTO taskDTO) {
        Task newTask = new Task();
        newTask.setTaskId(taskDTO.taskId());
        newTask.setSpaceId(space);
        newTask.setUserCreation(user);
        newTask.setUserResponsible(taskDTO.userResponsible());
        newTask.setTitle(taskDTO.title());
        newTask.setDescription(taskDTO.description());
        newTask.setDateCreation(taskDTO.dateCreation());
        newTask.setTimeCreation(taskDTO.timeCreation());
        newTask.setDateCompletion(taskDTO.dateCompletion());
        newTask.setTimeCompletion(taskDTO.timeCompletion());
        newTask.setAttachment(taskDTO.attachment());
        newTask.setCompleted(taskDTO.completed());

        taskRepository.save(newTask);
    }

    public void updateTask(User user, Space space, TaskDTO updatedTask) {
        Task existingTask = findTaskById(updatedTask.taskId());

        existingTask.setUserResponsible(updatedTask.userResponsible());
        existingTask.setTitle(updatedTask.title());
        existingTask.setDescription(updatedTask.description());
        existingTask.setDateCompletion(updatedTask.dateCompletion());
        existingTask.setTimeCompletion(updatedTask.timeCompletion());
        existingTask.setAttachment(updatedTask.attachment());
        existingTask.setCompleted(updatedTask.completed());

        taskRepository.save(existingTask);
    }

    public void deletedTask(User user, Space space, Long taskId) {
        Task existingTask = findTaskById(taskId);

        taskRepository.delete(existingTask);
    }

    public TaskDTO getTask(User user, Space space, Long taskId) {
        Task existingTask = findTaskById(taskId);

        return new TaskDTO(existingTask.getTaskId(), existingTask.getUserResponsible(), existingTask.getTitle(), existingTask.getDescription(),
                existingTask.getObservation(), existingTask.getDateCreation(), existingTask.getTimeCreation(), existingTask.getDateCompletion(),
                existingTask.getTimeCompletion(), existingTask.getDateEnd(), existingTask.getTimeEnd(), existingTask.getObservation(), existingTask.isCompleted());
    }

    public void validAccessLevelUser(User user, Space space) {
        UserSpaceRole userSpaceRole = spaceService.findUserRoleByUserAndSpace(user, space);

        if (userSpaceRole.getAccessLevel() == AccessLevel.INVITED) {
            //TODO Melhorar Excecao
            throw new DataAlreadyExistsException("Usuário sem acesso a tal funcionalidade");
        }
    }

    private Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task não encontrada"));
    }

}
