package campus_care.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${BREVO_SENDER_EMAIL}")
    private String fromEmail;
@Async
    public void sendEmail(
            String email,
            String subject,
            String body
    ) {

        try {

            log.info("Sending email...");
            log.info("From : {}", fromEmail);
            log.info("To   : {}", email);

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(fromEmail);

            mailSender.send(message);

            log.info("Email sent successfully.");

        } catch (Exception e) {

            log.error("Email sending failed", e);

            throw e;
        }
    }
}