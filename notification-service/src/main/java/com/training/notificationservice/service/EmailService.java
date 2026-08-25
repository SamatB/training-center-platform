package com.training.notificationservice.service;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.training.notificationservice.exception.EmailSendingException;
import org.springframework.mail.MailException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
        } catch (MailException exception) {
            throw new EmailSendingException("Failed to send email",
                    exception);
        }
    }


    }
