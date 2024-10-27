package ru.clevertec.sessionstarter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "session.blacklist")
public class BlackListProperties {
    private final Set<String> blackList = new HashSet<>();
}
