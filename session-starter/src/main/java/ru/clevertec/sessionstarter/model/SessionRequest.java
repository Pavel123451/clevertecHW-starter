package ru.clevertec.sessionstarter.model;

import lombok.Data;

@Data
public class SessionRequest {
    private String login;
    private Session session;
}
