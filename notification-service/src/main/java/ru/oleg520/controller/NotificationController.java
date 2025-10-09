package ru.oleg520.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.oleg520.dto.EmailSendResponse;
import ru.oleg520.dto.NewUserEventDto;
import ru.oleg520.service.EmailService;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public EmailSendResponse send(@RequestBody @Valid NewUserEventDto newUserEventDto) {
        return emailService.send(newUserEventDto);
    }
}
