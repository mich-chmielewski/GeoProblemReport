package pl.mgis.problemreport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.mgis.problemreport.model.dto.CommentDTO;
import pl.mgis.problemreport.model.dto.ReportDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//TODO Need to refactor this
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final TemplateEngine templateEngine;
    private final TaskExecutor taskExecutor;
    private final SettingService settingService;
    private final MailerService mailerService;

    public EmailService(TemplateEngine templateEngine, TaskExecutor taskExecutor,
                        SettingService settingService, MailerService mailerService) {
        this.templateEngine = templateEngine;
        this.taskExecutor = taskExecutor;
        this.settingService = settingService;
        this.mailerService = mailerService;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        if (settingService.isMailNotifyDisabled()) {
            return;
        }
        MimeMessage message = mailerService.mailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(settingService.getCompanySettingsMap().get("company_email"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            taskExecutor.execute(() -> mailerService.mailSender().send(message));
        } catch (RuntimeException | MessagingException e) {
            log.info("Error while sending email message {}", e.getMessage());
        }

    }

    public void sendRegistrationReportMessage(ReportDTO reportDTO) {
        sendTemplateReportMessage(reportDTO, null);
    }

    public void sendInformationReportMessage(ReportDTO reportDTO, CommentDTO commentDTO) {
        sendTemplateReportMessage(reportDTO, commentDTO);
    }

    public void sendTemplateReportMessage(ReportDTO reportDTO, CommentDTO commentDTO) {
        if (settingService.isMailNotifyDisabled()) {
            return;
        }
        MimeMessage message = mailerService.mailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(reportDTO.getreportTypeDTO().getEmail());
            helper.setReplyTo(reportDTO.getreportTypeDTO().getEmail());
            helper.setTo(new String[]{reportDTO.getEmail(), reportDTO.getreportTypeDTO().getEmail()});

            Context context = new Context();
            context.setVariable("reportDTO", reportDTO);
            context.setVariable("baseUrl", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
            context.setVariable("companyName", settingService.getCompanySettingsMap().get("company_name"));
            context.setVariable("removeEmail", reportDTO.getUuid());
            if (commentDTO != null) {
                if (commentDTO.getContent() != null) context.setVariable("commentDTO", commentDTO);
                helper.setSubject("Zgłoszenie " + reportDTO.getId() + " zostało zaktualizowane");
            } else {
                helper.setSubject("Zgłoszenie " + reportDTO.getId() + " zostało zarejestrowane");
            }
            String body = templateEngine.process("email-template", context);
            helper.setText(body, true);
            taskExecutor.execute(() -> mailerService.mailSender().send(message));
        } catch (RuntimeException | MessagingException e) {
            log.info("Error while sending email message {}", e.getMessage());
        }
    }


}
