package com.app.us_twogether.domain.reminder;

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
public class ReminderController {

    @Autowired
    ReminderService reminderService;

    @PostMapping("/{spaceId}/reminders")
    public ResponseEntity<ReminderResponseDTO> createReminder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long spaceId, @RequestBody @Valid ReminderRequestDTO reminder) {
        ReminderResponseDTO newReminder = reminderService.createReminder(userDetails.getUsername(), spaceId, reminder);

        return ResponseEntity.ok(newReminder);
    }

    @PutMapping("/{spaceId}/reminders/{reminderId}")
    public ResponseEntity<ReminderResponseDTO> updateReminder(@PathVariable Long reminderId, @RequestBody @Valid ReminderRequestDTO reminder) {
        ReminderResponseDTO updatedReminder = reminderService.updateReminder(reminderId, reminder);

        return ResponseEntity.ok(updatedReminder);
    }

    @PutMapping("/{spaceId}/reminders/{reminderId}/completed")
    public ResponseEntity<ReminderResponseDTO> completedReminder(@PathVariable Long reminderId) {
        ReminderResponseDTO reminder = reminderService.completedReminder(reminderId);

        return ResponseEntity.ok(reminder);
    }

    @DeleteMapping("/{spaceId}/reminders/{reminderId}")
    public ResponseEntity<String> deleteReminder(@PathVariable Long reminderId) {
        reminderService.deletedReminder(reminderId);

        return ResponseEntity.ok("Lembrete deletado com sucesso");
    }

    @GetMapping("/{spaceId}/reminders/{reminderId}")
    public ResponseEntity<ReminderResponseDTO> getReminder(@PathVariable Long reminderId) {
        ReminderResponseDTO reminder = reminderService.getReminder(reminderId);

        return ResponseEntity.ok(reminder);
    }

    @GetMapping("/{spaceId}/reminders")
    public ResponseEntity<List<ReminderResponseDTO>> getAllReminderFromSpace(@PathVariable Long spaceId, @RequestParam @DateTimeFormat LocalDate dateCompletion) {
        List<ReminderResponseDTO> reminders = reminderService.getAllRemindersFromSpace(spaceId, dateCompletion);

        return ResponseEntity.ok(reminders);
    }
}
