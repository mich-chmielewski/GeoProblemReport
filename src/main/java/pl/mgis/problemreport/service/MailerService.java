package pl.mgis.problemreport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Properties;

@Component
public class MailerService {

    Logger logger = LoggerFactory.getLogger(MailerService.class);

    private final SettingService settingService;

    public MailerService(SettingService settingService) {
        this.settingService = settingService;
    }

    public JavaMailSenderImpl mailSender() {
        Map<String, String> settings = settingService.getMailSettingsMap();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.get("mail_host"));
        mailSender.setPort(Integer.parseInt(settings.get("mail_port")));

        mailSender.setUsername(settings.get("mail_username"));
        mailSender.setPassword(settings.get("mail_password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", settings.get("mail_transport_protocol"));
        props.put("mail.smtp.auth", settings.get("mail_smtp_auth_boolean"));
        props.put("mail.smtp.starttls.enable", settings.get("mail_smtp_starttls_enable_boolean"));
        props.put("mail.debug", settings.get("mail_debug_boolean"));
        return mailSender;
    }

    public void testMailSettings(){
        try {
            mailSender().testConnection();
        } catch (MessagingException e) {
            logger.info("Test email settings Error {}",e.getMessage());
            settingService.disableMailNotify();
        }
    }
}
