package ru.clevertec.sessionstarter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.sessionstarter.properties.BlackListProperties;

@RequiredArgsConstructor
@Slf4j
public class DefaultBlackListProvider implements BlackListProvider {

    private final BlackListProperties blackListProperties;

    @Override
    public boolean isBlacklisted(String login) {
        log.info("Checking if login {} is blacklisted: {}", login, blackListProperties.getBlackList());
        return blackListProperties.getBlackList().contains(login);
    }
}
