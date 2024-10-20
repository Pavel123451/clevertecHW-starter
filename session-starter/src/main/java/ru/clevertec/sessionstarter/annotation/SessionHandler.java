package ru.clevertec.sessionstarter.annotation;

import ru.clevertec.sessionstarter.service.BlackListProvider;
import ru.clevertec.sessionstarter.service.DefaultBlackListProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionHandler {
    Class<? extends BlackListProvider> blacklistProvider()
            default DefaultBlackListProvider.class;
}
