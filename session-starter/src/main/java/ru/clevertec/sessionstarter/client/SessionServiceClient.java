package ru.clevertec.sessionstarter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.sessionstarter.model.Session;
import ru.clevertec.sessionstarter.model.SessionRequest;

@FeignClient(name = "session-service", url = "${session.service.url}")
public interface SessionServiceClient {

    @PostMapping("/sessions")
    Session getSession(@RequestBody SessionRequest request);
}

