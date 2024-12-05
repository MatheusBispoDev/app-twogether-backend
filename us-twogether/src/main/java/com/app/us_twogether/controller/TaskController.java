package com.app.us_twogether.controller;

import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.task.TaskDTO;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.base-url}/spaces/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("{user}/{space}/{taskDTO}")
    public ResponseEntity<String> createTask(@PathVariable User user, @PathVariable Space space, @PathVariable @Valid TaskDTO taskDTO) {

        taskService.validAccessLevelUser(user, space);
        taskService.createTask(user, space, taskDTO);

        return ResponseEntity.ok("Task criada com sucesso");
    }

    @PutMapping("{user}/{space}/{taskDTO}")
    public ResponseEntity<String> updateTask(@PathVariable User user, @PathVariable Space space, @PathVariable @Valid TaskDTO updatedtaskDTO) {

        taskService.validAccessLevelUser(user, space);
        taskService.updateTask(user, space, updatedtaskDTO);

        return ResponseEntity.ok("Task alterada com sucesso");
    }

    @DeleteMapping("{user}/{space}/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable User user, @PathVariable Space space, @PathVariable Long taskId) {

        taskService.deletedTask(user, space, taskId);

        return ResponseEntity.ok("Task deletada com sucesso");
    }

    @GetMapping("{user}/{space}/{taskId}")
    public ResponseEntity<String> getTask(@PathVariable User user, @PathVariable Space space, @PathVariable Long taskId) {

        taskService.getTask(user, space, taskId);

        return ResponseEntity.ok("Task criada com sucesso");
    }

}
