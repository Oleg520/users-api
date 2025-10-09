package ru.oleg520.service;

import ru.oleg520.dto.EmailSendResponse;
import ru.oleg520.dto.NewUserEventDto;

public interface EmailService {
    EmailSendResponse send(NewUserEventDto newUserEventDto);
}
