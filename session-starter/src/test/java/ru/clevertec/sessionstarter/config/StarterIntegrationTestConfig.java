package ru.clevertec.sessionstarter.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import ru.clevertec.sessionstarter.client.SessionServiceClient;
import ru.clevertec.sessionstarter.test_class.TestService;

import java.util.stream.Collectors;

@Configuration
@Import(SessionHandlerAutoConfiguration.class)
@EnableFeignClients(clients = SessionServiceClient.class)
public class StarterIntegrationTestConfig {

    @Bean
    public TestService testService() {
        return new TestService();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));

    }
}
