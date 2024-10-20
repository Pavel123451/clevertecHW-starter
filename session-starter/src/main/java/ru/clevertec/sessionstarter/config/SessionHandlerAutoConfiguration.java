package ru.clevertec.sessionstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.clevertec.sessionstarter.beanpp.SessionHandlerBeanPostProcessor;
import ru.clevertec.sessionstarter.listener.ApplicationStartListener;
import ru.clevertec.sessionstarter.properties.BlackListProperties;
import ru.clevertec.sessionstarter.service.BlackListProvider;
import ru.clevertec.sessionstarter.service.BlackListProviderHandler;
import ru.clevertec.sessionstarter.service.DefaultBlackListProvider;

import java.util.Set;

@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableAsync
@EnableConfigurationProperties(BlackListProperties.class)
@ConditionalOnProperty(value = "session.handler.enable", havingValue = "true")
@Slf4j
public class SessionHandlerAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("SessionHandlerAutoConfiguration is initialized");
    }

    @Bean
    public SessionHandlerBeanPostProcessor sessionHandlerBeanPostProcessor() {
        return new SessionHandlerBeanPostProcessor();
    }

    @Bean
    public BlackListProviderHandler blacklistProviderHandler(Set<BlackListProvider> blackListProviders) {
        return new BlackListProviderHandler(blackListProviders);
    }

    @Bean
    public DefaultBlackListProvider defaultBlacklistProvider(BlackListProperties blackListProperties) {
        return new DefaultBlackListProvider(blackListProperties);
    }

    @Bean
    public ApplicationStartListener applicationStartListener(BlackListProperties blackListProperties) {
        return new ApplicationStartListener(blackListProperties);
    }
}
