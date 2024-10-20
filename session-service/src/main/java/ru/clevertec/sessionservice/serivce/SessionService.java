package ru.clevertec.sessionservice.serivce;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.sessionservice.repository.SessionRepository;
import ru.clevertec.sessionservice.model.Session;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session createOrGetSession(String login) {
        Optional<Session> sessionOpt = getSession(login);
        if (sessionOpt.isPresent() && sessionOpt.get().getCloseAt().isAfter(LocalDateTime.now())) {
            return sessionOpt.get();
        }
        Session session = new Session(login);
        return sessionRepository.save(session);
    }

    public Optional<Session> getSession(String login) {
        return sessionRepository.findByLogin(login);
    }

    public void cleanupExpiredSessions() {
        sessionRepository.deleteByCloseAtBefore(LocalDateTime.now());
    }
}
