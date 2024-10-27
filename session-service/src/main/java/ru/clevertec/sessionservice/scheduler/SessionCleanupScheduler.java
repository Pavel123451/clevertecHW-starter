package ru.clevertec.sessionservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clevertec.sessionservice.serivce.SessionService;

@Component
@RequiredArgsConstructor
public class SessionCleanupScheduler {

    private final SessionService sessionService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanExpiredSessions() {
        sessionService.cleanupExpiredSessions();
    }
}
