package pl.mgis.problemreport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pl.mgis.problemreport.service.SettingService;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender configureMailSender() {
        return new JavaMailSenderImpl();
    }

}
