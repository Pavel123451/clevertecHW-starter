package ru.clevertec.sessionstarter.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Session {
    private String id;
    private String login;
    private LocalDateTime openedAt;
}

