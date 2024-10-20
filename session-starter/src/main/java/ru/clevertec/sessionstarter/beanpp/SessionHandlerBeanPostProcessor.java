package ru.clevertec.sessionstarter.beanpp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import ru.clevertec.sessionstarter.annotation.SessionHandler;
import ru.clevertec.sessionstarter.client.SessionServiceClient;
import ru.clevertec.sessionstarter.properties.BlackListProperties;
import ru.clevertec.sessionstarter.service.BlackListProviderHandler;
import ru.clevertec.sessionstarter.service.SessionHandlerInterceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SessionHandlerBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private final Map<String, Class<?>> beanNamesWithAnnotatedMethods = new HashMap<>();
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        boolean annotationPresent = Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(SessionHandler.class));
        if (annotationPresent) {
            log.info("Bean {} contains method(s) with @SessionHandler", beanName);
            beanNamesWithAnnotatedMethods.put(beanName, clazz);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return Optional.ofNullable(beanNamesWithAnnotatedMethods.get(beanName))
                .map(clazz -> getSessionProxy(bean))
                .orElse(bean);
    }

    private Object getSessionProxy(Object bean) {
        SessionServiceClient sessionServiceClient = beanFactory.getBean(SessionServiceClient.class);
        BlackListProviderHandler blacklistProviderHandler = beanFactory.getBean(BlackListProviderHandler.class);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new SessionHandlerInterceptor(
                bean,
                sessionServiceClient,
                blacklistProviderHandler,
                beanFactory)
        );
        return enhancer.create();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

