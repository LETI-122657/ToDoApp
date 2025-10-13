package com.example.emails;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    //Serve para enviar emails, mas não é usado na aplicação por enquanto


    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

@Configuration
class EmailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        // Dummy implementation que não envia emails, apenas evita o erro de bean ausente
        return new JavaMailSenderImpl() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                // Não faz nada
            }
            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {
                // Não faz nada
            }
        };
    }
}
