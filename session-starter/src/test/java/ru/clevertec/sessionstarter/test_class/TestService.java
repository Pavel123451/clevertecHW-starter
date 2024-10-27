package ru.clevertec.sessionstarter.test_class;

import ru.clevertec.sessionstarter.annotation.SessionHandler;
import ru.clevertec.sessionstarter.model.Session;
import ru.clevertec.sessionstarter.model.SessionRequest;


public class TestService {

    @SessionHandler
    public Session performAction(SessionRequest request) {
        return request.getSession();
    }

    @SessionHandler
    public Session performActionWithoutSessionRequest() {
        return new Session();
    }

    public Session performActionWithoutHandler() {
        return null;
    }
}
