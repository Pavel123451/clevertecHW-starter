package ru.clevertec.sessionservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "sessions")
public class Session {
    @Id
    private String id;
    private String login;
    private LocalDateTime openedAt;
    private LocalDateTime closeAt;

    public Session(String login) {
        this.login = login;
        this.openedAt = LocalDateTime.now();
        this.closeAt = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
    }
}
