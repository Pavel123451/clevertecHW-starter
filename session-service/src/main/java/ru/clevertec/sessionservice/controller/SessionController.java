package ru.clevertec.sessionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.sessionservice.model.Session;
import ru.clevertec.sessionservice.serivce.SessionService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/sessions")
    public ResponseEntity<Session> createSession(@RequestParam String login) {
        Session session = sessionService.createOrGetSession(login);
        return ResponseEntity.ok(session);
    }
}
