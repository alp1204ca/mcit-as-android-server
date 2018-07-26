package com.mcit.AdmissionSystem.service;


import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import java.util.Locale;
import java.util.Map;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Value("${mail.from}")
    String defaultFrom;

    @Async
    public void send(String from, String to, String subject, String template, Map<String, Object> params) {
        log.info("sending email {} with subject '{}' to {}...", template, subject, to);

        Context context = new Context(Locale.getDefault(), params);
        String htmlContent = templateEngine.process(template, context);
        String textContent = Jsoup.parse(htmlContent).text();

        MimeMessage message = mailSender.createMimeMessage();

        try {
        	message.setSubject(subject);
            message.setFrom(new InternetAddress(from));
            message.addRecipients(RecipientType.TO, InternetAddress.parse(to));

            // Unformatted text version
            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(textContent, "text/plain"); 
            // HTML version
            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html");
            
            final Multipart mp = new MimeMultipart("alternative");
            mp.addBodyPart(textPart);
            mp.addBodyPart(htmlPart);
            message.setContent(mp);
            
            mailSender.send(message);
            
            log.info("Mail sent successfully from " + from + " to  " + to + " with subject " + subject);
        } catch (Exception e) {
            log.error("Failed to send email from " + from + " to  " + to + " with subject " + subject, e);
        }
    }
}
