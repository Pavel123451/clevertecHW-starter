package ru.clevertec.sessionstarter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.sessionstarter.model.Session;

@FeignClient(name = "session-service", url = "${session.service.url}")
public interface SessionServiceClient {

    @PostMapping("/sessions")
    Session getSession(@RequestParam("login") String login);
}

