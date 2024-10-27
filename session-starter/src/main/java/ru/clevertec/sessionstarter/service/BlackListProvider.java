package ru.clevertec.sessionstarter.service;

public interface BlackListProvider {

    boolean isBlacklisted(String login);
}
