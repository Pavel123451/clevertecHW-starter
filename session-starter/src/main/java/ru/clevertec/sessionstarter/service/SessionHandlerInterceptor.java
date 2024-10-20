package ru.clevertec.sessionstarter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import ru.clevertec.sessionstarter.annotation.SessionHandler;
import ru.clevertec.sessionstarter.client.SessionServiceClient;
import ru.clevertec.sessionstarter.model.SessionRequest;
import ru.clevertec.sessionstarter.model.Session;

import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class SessionHandlerInterceptor implements MethodInterceptor {

    private final Object originalBean;
    private final SessionServiceClient sessionServiceClient;
    private final BlackListProviderHandler blacklistProviderHandler;
    private final BeanFactory beanFactory;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.info("Intercepting method: {} of class: {}", method.getName(),
                originalBean.getClass().getSimpleName());

        if (method.isAnnotationPresent(SessionHandler.class)) {
            String login = extractLoginFromArgs(args);
            if (login == null) {
                log.error("Login not found in method arguments for method: {}", method.getName());
                throw new IllegalArgumentException("Login must be provided in method arguments.");
            }
            log.info("Login extracted: {}", login);

            if (blacklistProviderHandler.isBlacklisted(login)) {
                log.warn("Login {} is blacklisted. Throwing exception for method: {}",
                        login, method.getName());
                throw new IllegalStateException("The login is blacklisted: " + login);
            }
            log.info("Login {} passed blacklist check", login);

            log.info("Attempting to retrieve or create session for login: {}", login);
            Session session = sessionServiceClient.getSession(login);
            log.info("Session retrieved/created with id: {} for login: {}", session.getId(), login);

            injectSessionIntoArgs(args, session);
            log.info("Session injected into method arguments for login: {}", login);

            Object result = method.invoke(originalBean, args);
            log.info("Method {} executed successfully", method.getName());

            return result;
        }

        log.info("Method {} is not annotated with @SessionHandler, executing without session handling.",
                method.getName());
        return method.invoke(originalBean, args);
    }

    private String extractLoginFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof SessionRequest) {
                return ((SessionRequest) arg).getLogin();
            }
        }
        return null;
    }

    private void injectSessionIntoArgs(Object[] args, Session session) {
        for (Object arg : args) {
            if (arg instanceof SessionRequest) {
                ((SessionRequest) arg).setSession(session);
            }
        }
    }
}
