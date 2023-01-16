package pl.mgis.problemreport.controller.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mgis.problemreport.model.dto.SettingObjDTO;
import pl.mgis.problemreport.model.dto.SettingsFormDTO;
import pl.mgis.problemreport.model.dto.SettingsFormListDTO;
import pl.mgis.problemreport.service.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("/")
public class ApplicationTemplateController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String SUCCESS_MSG_TYPE = "Success";
    private static final String WARNING_MSG_TYPE = "Warning";

    public final ReportTypeService reportTypeService;
    private final UserService userService;
    private final ReportStatusService reportStatusService;
    private final ReportService reportService;
    private final SettingService settingService;
    private final MailerService mailerService;

    public ApplicationTemplateController(ReportTypeService reportTypeService, UserService userService,
                                         ReportStatusService reportStatusService,
                                         SettingService settingService, ReportService reportService,
                                         MailerService mailerService) {
        this.reportTypeService = reportTypeService;
        this.userService = userService;
        this.reportStatusService = reportStatusService;
        this.settingService = settingService;
        this.reportService = reportService;
        this.mailerService = mailerService;
    }

    @GetMapping("manager/")
    public String index(Model model) {
        model.addAttribute("pageContent", null);
        return "adm/main";
    }

    @GetMapping("manager/{page}")
    public String index(@PathVariable("page") String page, Model model, @ModelAttribute("msg") String msg,
                        @ModelAttribute("info") String info) {
        model.addAttribute("pageContent", null);
        if (!page.isBlank()) {
            model.addAttribute("pageContent", page);
            model.addAttribute("msg", msg);
            model.addAttribute("info", info);
        }
        return "adm/main";
    }

    @PostMapping({"manager/dashboard"})
    public String dashboard(Model model) {
        List<Map<String, Integer>> reportsStatistic = reportService.getReportsStatistic();
        model.addAttribute("typesStatistic", reportsStatistic.get(0));
        model.addAttribute("statusStatistic", reportsStatistic.get(1));
        return "adm/main-content :: main-dashboard";
    }

    @PostMapping("manager/reporttype")
    public String getAllReportTypes(Model model) {
        model.addAttribute("reportTypes", reportTypeService.getAll());
        return "adm/main-content :: main-reportTypes";
    }

    @PostMapping("manager/status")
    public String getAllReportStatus(Model model) {
        model.addAttribute("reportStatus", reportStatusService.getAll());
        return "adm/main-content :: main-reportStatus";
    }

    @PostMapping("manager/user")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "adm/main-content :: main-users";
    }

    @PostMapping("manager/report")
    public String getAllReports() {
        return "adm/main-content :: main-reports";
    }

    @PostMapping("manager/mail")
    public String getMailSettings(Model model) {
        model.addAttribute("mailSettingsForm",
                new SettingsFormListDTO(settingService.getMailSettingsListWithDisplayName()));
        return "adm/main-content :: main-settings-mail";
    }

    @PostMapping("manager/mail/save")
    public String submitMailSettingsForm(@ModelAttribute SettingsFormListDTO mailSettingsForm,
                                         RedirectAttributes redirectAttrs) {
        Map<String, String> newSettings = new HashMap<>();
        final StringBuilder info = new StringBuilder();
        String message = SUCCESS_MSG_TYPE;

        for (SettingObjDTO settingObjDTO : mailSettingsForm.getSettingObjDTOS()) {
            newSettings.put(settingObjDTO.getKey(), settingObjDTO.getValue());
        }
        settingService.updateSettings(newSettings);
        if (!settingService.isMailNotifyDisabled()) {
            mailerService.testMailSettings();
        }
        if (settingService.isMailNotifyDisabled()) {
            info.append("Wrong email settings; test connection failed;");
            log.info(info.toString());
            message = WARNING_MSG_TYPE;
        }
        redirectAttrs.addFlashAttribute("msg", message);
        redirectAttrs.addFlashAttribute("info", info);
        return "redirect:/manager/mail";
    }

    @PostMapping("manager/company")
    public String getCompanySettingsToEdit(Model model) {
        SettingsFormDTO settingsForm = new SettingsFormDTO(settingService.getCompanySettingsMap());
        model.addAttribute("settingsForm", settingsForm);
        return "adm/main-content :: main-settings-company";
    }

    @PostMapping("manager/company/save")
    public String submitCompanySettingsForm(@RequestParam("company_json_borders_file") MultipartFile bordersFile,
                                            @RequestParam("company_json_roads_file") MultipartFile roadsFile,
                                            @RequestParam("company_logo_file") MultipartFile logoFile,
                                            @ModelAttribute SettingsFormDTO settingsForm,
                                            RedirectAttributes redirectAttrs) {
        var map = settingsForm.getSettings();
        final StringBuilder info = new StringBuilder();
        String message = SUCCESS_MSG_TYPE;

        if (!logoFile.isEmpty()) {
            try {
                map.put("company_logo_file", settingService.saveFile(logoFile));
            } catch (IOException e) {
                info.append("Problem with saving file; ");
                log.info(info.toString());
                message = WARNING_MSG_TYPE;
            }
        }

        if (!roadsFile.isEmpty()) {
            try {
                settingService.checkJsonFormat(roadsFile);
                map.put("company_json_roads_file", settingService.saveFile(roadsFile));
            } catch (IOException e) {
                info.append("Wrong GeoJson road file format; ");
                log.info(info.toString());
                message = WARNING_MSG_TYPE;
            }
        }
        if (!bordersFile.isEmpty()) {
            try {
                settingService.checkJsonFormat(bordersFile);
                map.put("company_json_borders_file", settingService.saveFile(bordersFile));
            } catch (IOException e) {
                info.append("Wrong GeoJson border file format; ");
                message = WARNING_MSG_TYPE;
            }
        }
        settingService.updateSettings(map);
        redirectAttrs.addFlashAttribute("msg", message);
        redirectAttrs.addFlashAttribute("info", info);
        return "redirect:/manager/company";
    }

}
