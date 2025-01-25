package com.app.us_twogether.controller;

import com.app.us_twogether.domain.reminder.ReminderDTO;
import com.app.us_twogether.domain.reminder.ReminderRequestDTO;
import com.app.us_twogether.service.RemindersService;
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
public class RemindersController {

    @Autowired
    RemindersService remindersService;

    @PostMapping("/{spaceId}/reminders")
    public ResponseEntity<ReminderDTO> createReminders(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long spaceId, @RequestBody @Valid ReminderRequestDTO reminderDTO) {
        ReminderDTO newReminders = remindersService.createReminders(userDetails.getUsername(), spaceId, reminderDTO);

        return ResponseEntity.ok(newReminders);
    }

    @PutMapping("/{spaceId}/reminders/{reminderId}")
    public ResponseEntity<ReminderDTO> updateReminders(@PathVariable Long reminderId, @RequestBody @Valid ReminderRequestDTO updatedReminderDTO) {
        ReminderDTO reminders = remindersService.updateReminders(reminderId, updatedReminderDTO);

        return ResponseEntity.ok(reminders);
    }

    @PutMapping("/{spaceId}/reminders/{reminderId}/completed")
    public ResponseEntity<ReminderDTO> completedReminders(@PathVariable Long reminderId) {
        ReminderDTO reminder = remindersService.completedReminders(reminderId);

        return ResponseEntity.ok(reminder);
    }

    @DeleteMapping("/{spaceId}/reminders/{reminderId}")
    public ResponseEntity<String> deleteReminders(@PathVariable Long reminderId) {
        remindersService.deletedReminders(reminderId);

        return ResponseEntity.ok("Reminders deletada com sucesso");
    }

    @GetMapping("/{spaceId}/reminders/{reminderId}")
    public ResponseEntity<ReminderDTO> getReminders(@PathVariable Long reminderId) {
        ReminderDTO reminders = remindersService.getReminders(reminderId);

        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/{spaceId}/reminders")
    public ResponseEntity<List<ReminderDTO>> getAllRemindersFromSpace(@PathVariable Long spaceId, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCompletion) {
        List<ReminderDTO> reminders = remindersService.getAllRemindersFromSpace(spaceId, dateCompletion);

        return ResponseEntity.ok(reminders);
    }
}
