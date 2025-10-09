package ru.oleg520.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.oleg520.dto.EmailSendResponse;
import ru.oleg520.dto.NewUserEventDto;
import ru.oleg520.dto.UserOperationType;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailTemplatesProperties templates;

    @Override
    public EmailSendResponse send(NewUserEventDto newUserEventDto) {
        String email = newUserEventDto.email();
        UserOperationType operation = newUserEventDto.operation();

        EmailTemplatesProperties.Template template = switch (operation) {
            case CREATE -> templates.getCreate();
            case DELETE -> templates.getDelete();
        };

        log.info("Отправка email: To={}, Subject={}, Message={}", email, template.getSubject(), template.getMessage());
        return new EmailSendResponse(email, template.getSubject(), template.getMessage());
    }
}
