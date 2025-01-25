package com.app.us_twogether.domain.task;

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
    public ResponseEntity<TaskResponseDTO> createTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long spaceId, @RequestBody @Valid TaskRequestDTO task) {
        TaskResponseDTO newTask = taskService.createTask(userDetails.getUsername(), spaceId, task);

        return ResponseEntity.ok(newTask);
    }

    @PutMapping("/{spaceId}/task/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long taskId, @RequestBody @Valid TaskRequestDTO task) {
        TaskResponseDTO updatedTask = taskService.updateTask(taskId, task);

        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{spaceId}/task/{taskId}/completed")
    public ResponseEntity<TaskResponseDTO> completedTask(@PathVariable Long taskId) {
        TaskResponseDTO task = taskService.completedTask(taskId);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{spaceId}/task/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deletedTask(taskId);

        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }

    @GetMapping("/{spaceId}/task/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long taskId) {
        TaskResponseDTO task = taskService.getTask(taskId);

        return ResponseEntity.ok(task);
    }

    @GetMapping("/{spaceId}/task")
    public ResponseEntity<List<TaskResponseDTO>> getAllTaskFromSpace(@PathVariable Long spaceId, @RequestParam(required = false) @DateTimeFormat LocalDate dateCompletion) {
        List<TaskResponseDTO> tasks;

        if (dateCompletion != null){
            tasks = taskService.getAllTaskFromSpaceAndDate(spaceId, dateCompletion);
        }else {
            tasks = taskService.getAllTaskFromSpace(spaceId);
        }

        return ResponseEntity.ok(tasks);
    }

}
