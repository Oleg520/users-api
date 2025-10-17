package ru.oleg520;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.oleg520.controller.NotificationController;
import ru.oleg520.dto.NewUserEventDto;
import ru.oleg520.dto.event.UserOperationType;
import ru.oleg520.service.EmailService;

import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailService emailService;

    @Test
    void testSendValidRequest() throws Exception {
        String body = """
                {
                  "email": "test@example.com",
                  "operation": "CREATE"
                }
                """;

        mockMvc.perform(post("/send")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(emailService).send(new NewUserEventDto("test@example.com", UserOperationType.CREATE));
    }

    @Test
    void testSendInvalidEmail() throws Exception {
        String body = """
                {
                  "email": "invalid",
                  "operation": "CREATE"
                }
                """;

        mockMvc.perform(post("/send")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
