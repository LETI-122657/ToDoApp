package com.example.emails;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // Serve para não dar erro apenas, já que o JavaMailSender é necessário para o EmailService
        // Configuração mínima para evitar erro de bean ausente
        return new JavaMailSenderImpl();
    }
}

