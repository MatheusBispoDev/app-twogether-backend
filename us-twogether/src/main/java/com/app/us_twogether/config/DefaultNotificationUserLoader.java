package com.app.us_twogether.config;

import com.app.us_twogether.domain.notificationUser.NotificationUser;
import com.app.us_twogether.repository.NotificationUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultNotificationUserLoader {
    @Getter
    private static NotificationUser defaultNotificationUser;

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @PostConstruct
    public void init() {
        if (notificationUserRepository.count() == 0) {
            notificationUserRepository.save(new NotificationUser(1, "TOTAL", "Será notificado todas as atualizações no sistema"));
            notificationUserRepository.save(new NotificationUser(2, "SOMENTE ATIVIDADES", "Será notificado somente o contexto de atividades"));
            notificationUserRepository.save(new NotificationUser(3, "SOMENTE PLANEJAMENTO FINANCEIRO", "Será notificado somente o contexto de atualizações financeiras"));
        }

        defaultNotificationUser = notificationUserRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Notificação padrão não encontrada"));
    }

}
