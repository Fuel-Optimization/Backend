package com.example.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(List<String> toEmails, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmails.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (MailException e) {
            System.err.println("Error while sending email: " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    }

