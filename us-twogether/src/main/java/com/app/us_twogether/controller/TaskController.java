package com.app.us_twogether.controller;

import com.app.us_twogether.domain.task.TaskDTO;
import com.app.us_twogether.domain.task.TaskRequestDTO;
import com.app.us_twogether.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${app.api.base-url}/spaces")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/{spaceId}/task")
    public ResponseEntity<TaskDTO> createTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long spaceId, @RequestBody @Valid TaskRequestDTO taskDTO) {
        TaskDTO newTask = taskService.createTask(userDetails.getUsername(), spaceId, taskDTO);

        return ResponseEntity.ok(newTask);
    }

    @PutMapping("/{spaceId}/task/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody @Valid TaskRequestDTO updatedtaskDTO) {
        TaskDTO task = taskService.updateTask(taskId, updatedtaskDTO);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{spaceId}/task/{taskId}/completed")
    public ResponseEntity<TaskDTO> completedTask(@PathVariable Long taskId) {
        TaskDTO task = taskService.completedTask(taskId);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{spaceId}/task/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deletedTask(taskId);

        return ResponseEntity.ok("Task deletada com sucesso");
    }

    @GetMapping("/{spaceId}/task/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTask(taskId);

        return ResponseEntity.ok(task);
    }

    @GetMapping("/{spaceId}/task")
    public ResponseEntity<List<TaskDTO>> getAllTaskFromSpace(@PathVariable Long spaceId, @RequestParam() @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCompletion) {
        List<TaskDTO> tasks = taskService.getAllTaskFromSpace(spaceId, dateCompletion);

        return ResponseEntity.ok(tasks);
    }

}
