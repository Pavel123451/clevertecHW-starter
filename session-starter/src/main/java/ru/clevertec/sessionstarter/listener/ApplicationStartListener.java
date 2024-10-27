package ru.clevertec.sessionstarter.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.clevertec.sessionstarter.properties.BlackListProperties;

@Slf4j
@RequiredArgsConstructor
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {
    private final BlackListProperties blackListProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Session starter started " + blackListProperties.getBlackList());
    }
}
