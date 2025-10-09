package ru.oleg520.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.email.templates")
@Getter
@Setter
public class EmailTemplatesProperties {
    private Template create;
    private Template delete;

    @Getter
    @Setter
    public static class Template {
        private String subject;
        private String message;
    }
}
