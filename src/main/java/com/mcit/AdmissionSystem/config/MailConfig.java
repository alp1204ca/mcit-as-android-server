package com.mcit.AdmissionSystem.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailConfig.MailProperties.class)
public class MailConfig {

    @Autowired
    MailProperties mailProperties;
    
    @Autowired
    Environment env;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(mailProperties.getUser());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        if(mailProperties.getHost().contains("smtp"))
            properties.put("mail.transport.protocol", "smtp");

        if(mailProperties.getPort()!=25 && mailProperties.getPort()!=587)
        {
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "false");
            properties.put("mail.smtp.socketFactory.port", mailProperties.getPort());
        }
        mailSender.setJavaMailProperties(properties);


        return mailSender;
    }

    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("mail/");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);
        return templateResolver;
    }

    @ConfigurationProperties(value = "mail")
    public static class MailProperties {

        private String host;
        private int port;
        private String user;
        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
