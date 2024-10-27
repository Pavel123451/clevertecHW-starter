package ru.clevertec.sessionstarter;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import ru.clevertec.sessionstarter.config.StarterIntegrationTestConfig;
import ru.clevertec.sessionstarter.model.Session;
import ru.clevertec.sessionstarter.model.SessionRequest;
import ru.clevertec.sessionstarter.test_class.TestService;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@WireMockTest(httpPort = 9090)
@SpringBootTest(classes = {StarterIntegrationTestConfig.class, FeignAutoConfiguration.class})
@TestPropertySource(properties = {
        "session.service.url=http://localhost:9090/api",
        "session.handler.enable=true",
        "session.blacklist.black-list=bluser1,bluser2,bluser3"
})
public class StarterIntegrationTest {

    @Autowired
    private TestService testService;

    @BeforeEach
    void setupMock() {
        WireMock.stubFor(post(urlEqualTo("/api/sessions"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": "test-session-id",
                                    "login": "testUser",
                                    "openedAt": "2024-10-26T23:50:59.683",
                                    "closeAt": "2024-10-26T23:59:59.683"
                                }
                                """)));
    }

    @AfterEach
    void verifyInteractions() {
        WireMock.reset();
    }

    @Test
    void testSessionHandlerWithValidLogin() {
        SessionRequest request = new SessionRequest();
        request.setLogin("testUser");

        Session result = testService.performAction(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("test-session-id");
        assertThat(result.getLogin()).isEqualTo("testUser");

        verify(1, postRequestedFor(urlEqualTo("/api/sessions"))
                .withRequestBody(matchingJsonPath("$.login", equalTo("testUser"))));
    }

    @Test
    void testSessionHandlerWithBlacklistedLogin() {
        SessionRequest request = new SessionRequest();
        request.setLogin("bluser1");

        assertThatThrownBy(() -> testService.performAction(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The login is blacklisted: bluser1");

        verify(0, postRequestedFor(urlEqualTo("/api/sessions")));
    }

    @Test
    void testSessionHandlerWithoutSessionRequest() {
        assertThatThrownBy(() -> testService.performActionWithoutSessionRequest())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Login must be provided in method arguments.");

        verify(0, postRequestedFor(urlEqualTo("/api/sessions")));
    }

    @Test
    void testMethodWithoutSessionHandler() {
        Session result = testService.performActionWithoutHandler();

        assertThat(result).isNull();

        verify(0, postRequestedFor(urlEqualTo("/api/sessions")));
    }
}










