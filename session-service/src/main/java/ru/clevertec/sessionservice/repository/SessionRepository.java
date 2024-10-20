package ru.clevertec.sessionservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.clevertec.sessionservice.model.Session;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionRepository extends MongoRepository<Session, String> {
    Optional<Session> findByLogin(String login);
    void deleteByCloseAtBefore(LocalDateTime now);
}
