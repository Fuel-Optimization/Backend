package com.example.service;
import com.example.model.model.Manager;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    Logger logger = Logger.getLogger(EmailService.class.getName());

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
    public void TriggerEmail(Manager manager, String DriverFullName, double avgFuelConsumption) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String currentDate = dateFormat.format(new Date());

        String subject = "Fuel Consumption Alert";
        String body = "Dear " + manager.getUser().getFirstName() + ",\n\n" +
                "Driver "  +DriverFullName+
                " has exceeded the expected fuel consumption"+  " At " + currentDate + "\n\n" +"with an average of " +
                String.format("%.2f", avgFuelConsumption) + " liters per unit over the recent period.\n\n" +
                "Please take necessary action.\n\n" +
                "Best regards,\nFuel Monitoring System";
        String managerEmail = manager.getUser().getEmail();
   sendEmail(Collections.singletonList(managerEmail), subject, body);
        logger.info("Email sent to: " + managerEmail);
        logger.info(DriverFullName);
    }

    }

