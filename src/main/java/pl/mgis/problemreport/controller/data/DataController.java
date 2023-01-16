package pl.mgis.problemreport.controller.data;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.mgis.problemreport.config.MvcConfiguration;
import pl.mgis.problemreport.service.SettingService;
import pl.mgis.problemreport.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;

@ApiIgnore
@Controller
@ResponseBody
@RequestMapping("/data")
public class DataController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final SettingService settingService;

    public DataController(SettingService settingService) {
        this.settingService = settingService;
    }


    @GetMapping(value = "/border")
    public ResponseEntity<byte[]> getBorder() throws IOException {
        byte[] file = getFileFromSettings("company_json_borders_file", "poland-border.geojson");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(file);
    }

    @GetMapping(value = "/roads")
    public ResponseEntity<byte[]> getRoads() throws IOException {
        byte[] file = getFileFromSettings("company_json_roads_file", "poland-roads.geojson");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(file);
    }

    @GetMapping(value = "/logo")
    public  ResponseEntity<byte[]> getLogo() throws IOException {
        byte[] file = getFileFromSettings("company_logo_file", "logo-nn.png");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(file);
    }

    private byte[] getFileFromSettings(String settingKey, String fileName) throws IOException {
        String settingValue = settingService.getByKey(settingKey).getValue();
        if (settingValue == null) {
            ClassPathResource classPathResource = new ClassPathResource("data/" + fileName);
            return classPathResource.getInputStream().readAllBytes();
        }
        return FileUtils.readFileToByteArray(new File(MvcConfiguration.DATA_DIR + settingValue));
    }
}
