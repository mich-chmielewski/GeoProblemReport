package pl.mgis.problemreport.controller.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.mgis.problemreport.exception.CustomerEmailNotFoundException;
import pl.mgis.problemreport.exception.UserNotFoundException;
import pl.mgis.problemreport.model.dto.ReportDTO;
import pl.mgis.problemreport.model.dto.ReportTypeDTO;
import pl.mgis.problemreport.service.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@ApiIgnore
@Controller
@RequestMapping("/")
public class PublicTemplateController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final ReportTypeService reportTypeService;
    private final ReportStatusService reportStatusService;
    private final ReportService reportService;
    private final SettingService settingsService;
    private final EmailService emailService;
    private final UserService userService;


    public PublicTemplateController(ReportTypeService reportTypeService, ReportStatusService reportStatusService, ReportService reportService, SettingService settingsService, EmailService emailService, UserService userService) {
        this.reportTypeService = reportTypeService;
        this.reportStatusService = reportStatusService;
        this.reportService = reportService;
        this.settingsService = settingsService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        if (userService.isUserLoggedIn())
            return "redirect:/manager/";
        model.addAttribute("settings", settingsService.getCompanySettingsMap());
        return "login";
    }

    @GetMapping(value = "/authorize")
    @ResponseBody
    public boolean isUserLoggedIn() {
        return userService.isUserLoggedIn();
    }

    @GetMapping("/reset")
    public String showResetPasswordPage(Model model) {
        model.addAttribute("settings", settingsService.getCompanySettingsMap());
        return "reset";
    }

    @PostMapping("/reset")
    public String submitResetPassword(Model model, @RequestParam(name = "usermail") String userEmail) {
        String message = "Twoje hasło zostało zresetowane, sprawdź pocztę email!";
        try {
            userService.resetPassword(userEmail);
        } catch (UserNotFoundException e) {
            log.info("User with " + userEmail + " - not exist");
            message = "Brak wskazanego adresu email w aplikacji!";
        }
        model.addAttribute("settings", settingsService.getCompanySettingsMap());
        model.addAttribute("info", message);
        return "reset";
    }

    @GetMapping({"/", "/index*"})
    public String showReportForm(Model model) {
        model.addAttribute("report", new ReportDTO());
        model.addAttribute("reportTypes", reportTypeService.getAll());
        model.addAttribute("settings", settingsService.getCompanySettingsMap());
        return "index";
    }

    @PostMapping({"/", "/index*"})
    public String submitReportForm(@Valid @ModelAttribute("report") ReportDTO reportDTO,
                                   BindingResult bindingResult, Model model, @PathParam("reportType") int reportType) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("report", new ReportDTO());
            model.addAttribute("reportTypes", reportTypeService.getAll());
            model.addAttribute("settings", settingsService.getCompanySettingsMap());
            final StringBuilder builder = new StringBuilder();
            builder.append("Zgłoszenie nie zostało wysłane; Pojawiły się błedy: ");
            bindingResult.getFieldErrors().forEach(e -> builder.append(e.getDefaultMessage()).append(", "));
            model.addAttribute("error", builder);
            log.info("BINDING RESULT ERROR: {}", builder);
            return "index";
        }
        ReportTypeDTO reportTypeDTO = new ReportTypeDTO();
        reportTypeDTO.setId(reportType);
        reportDTO.setReportStatusDTO(reportStatusService.getFirstStage());
        reportDTO.setReportTypeDTO(reportTypeDTO);
        ReportDTO savedReport = reportService.saveForm(reportDTO);
        model.addAttribute("reportDTO", savedReport);
        model.addAttribute("settings", settingsService.getCompanySettingsMap());
        emailService.sendRegistrationReportMessage(savedReport);
        return "report-result";
    }


    @GetMapping("unregister/{report}")
    public String confirmDeleteForm(@PathVariable(value = "report") String uuid, Model model) {
        log.info("Form for confirmation delete email address in report: {}", uuid);
        model.addAttribute("settings", settingsService.getCompanySettingsMap());
        model.addAttribute("uuid", uuid);
        model.addAttribute("type", "form");
        try {
            reportService.deleteEmailInReports(uuid);
        } catch (CustomerEmailNotFoundException e) {
            log.info("No email address found in reports: {}", e.getMessage());
            return "unregister-not-found";
        }
        return "unregister-result";
    }

    @PostMapping("unregister")
    public String deleteForm(@RequestParam(name = "uuid") String uuid, Model model) {
        model.addAttribute("settings", settingsService.getCompanySettingsMap());
        model.addAttribute("type", "result");
        try {
            reportService.deleteEmailInReports(uuid);
            log.info("Customer confirmed deleting email address: {}", uuid);
        } catch (CustomerEmailNotFoundException e) {
            log.info("No email address found in reports: {}", e.getMessage());
        }
        return "unregister-result";
    }
}
