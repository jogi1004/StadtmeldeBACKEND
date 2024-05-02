package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.UserEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserService userService;

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${website.link}")
    private String weblink;

    @Async
    public void sendWelcomeEmail(String toEmail, String username) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("weblink", weblink);
        String text = templateEngine.process("WelcomeMailTemplate.html", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(5);
        helper.setSubject("Willkommen bei CityCare!");
        helper.setFrom(emailFrom);
        helper.setTo(toEmail);
        helper.setText(text, true);
        mailSender.send(message);
    }

    @Async
    public void sendStatusChangeEmail(String toEmail, String username, String reportTitle, String status, String date,
            String image) throws MessagingException, NotFoundException {

        UserEntity user = userService.getUserEntityByUsername(username);
        if (user.isNotificationsEnabled()) {

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("reportTitle", reportTitle);
            context.setVariable("status", status);
            context.setVariable("date", date);
            context.setVariable("image", image);
            context.setVariable("weblink", weblink);
            String text = templateEngine.process("StatusChangeMailTemplate.html", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(4);
            helper.setSubject("Statusänderung deiner Meldung");
            helper.setFrom(emailFrom);
            helper.setTo(toEmail);
            helper.setText(text, true);
            mailSender.send(message);
        }
    }
}