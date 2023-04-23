package com.example.redux.service;


import com.example.redux.ReduxApplication;
import com.example.redux.exceptions.EmailException;
import com.example.redux.model.NotificationEmail;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final MailBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
            MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("redux@geoco.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!");
        }catch (MailException e){
            log.error(e.getMessage());
            throw new EmailException("Error occurred when sending the email to: " + notificationEmail.getRecipient());
        }
    }
}
