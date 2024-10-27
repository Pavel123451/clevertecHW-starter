package ru.clevertec.sessionservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.sessionservice.config.MongoDBContainerConfiguration;
import ru.clevertec.sessionservice.dto.SessionRequest;
import ru.clevertec.sessionservice.model.Session;
import ru.clevertec.sessionservice.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = MongoDBContainerConfiguration.class)
public class SessionControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        sessionRepository.deleteAll();
        restTemplate = restTemplateBuilder.rootUri("http://localhost:" + port).build();
    }

    @Test
    public void createSession() {
        String login = "user123";
        SessionRequest request = new SessionRequest();
        request.setLogin(login);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SessionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Session> response = restTemplate.exchange(
                "/api/sessions", HttpMethod.POST, entity, Session.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLogin()).isEqualTo(login);

        Optional<Session> session = sessionRepository.findByLogin(login);
        assertThat(session).isPresent();
        assertThat(session.get().getOpenedAt()).isNotNull();
        assertThat(session.get().getCloseAt()).isAfter(LocalDateTime.now());
    }

    @Test
    public void createSessionExistsAndNotExpired() {
        String login = "user456";
        Session existingSession = new Session(login);
        sessionRepository.save(existingSession);

        SessionRequest request = new SessionRequest();
        request.setLogin(login);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SessionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Session> response = restTemplate.exchange(
                "/api/sessions", HttpMethod.POST, entity, Session.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(existingSession.getId());
    }

    @Test
    public void cleanExpiredSessions() {
        String login = "userExpired";
        Session expiredSession = new Session(login);
        expiredSession.setCloseAt(LocalDateTime.now().minusDays(1));
        sessionRepository.save(expiredSession);

        sessionRepository.deleteByCloseAtBefore(LocalDateTime.now());

        Optional<Session> session = sessionRepository.findByLogin(login);
        assertThat(session).isEmpty();
    }
}

