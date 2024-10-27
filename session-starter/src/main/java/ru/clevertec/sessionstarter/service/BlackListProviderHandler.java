package ru.clevertec.sessionstarter.service;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class BlackListProviderHandler {

    private final Set<? extends BlackListProvider> blacklistProviders;

    public boolean isBlacklisted(String login) {
        return blacklistProviders.stream()
                .anyMatch(provider -> provider.isBlacklisted(login));
    }
}
