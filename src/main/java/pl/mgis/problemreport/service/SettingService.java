package pl.mgis.problemreport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.mgis.problemreport.config.MvcConfiguration;
import pl.mgis.problemreport.exception.ResourceNotFoundException;
import pl.mgis.problemreport.model.Setting;
import pl.mgis.problemreport.model.dto.SettingObjDTO;
import pl.mgis.problemreport.repository.SettingRepository;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SettingService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final SettingRepository settingRepository;


    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public Setting get(long id) {
        return settingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Setting not found"));
    }

    public Setting getByKey(String key) {
        return settingRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException("Setting not found"));
    }

    public Map<String, String> getMailSettingsMap() {
        return settingRepository.findAll()
                .stream()
                .filter(s -> s.getKey().startsWith("mail"))
                .collect(HashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), Map::putAll);
    }

    public boolean isMailNotifyDisabled() {
        return !settingRepository.findByKey("mail_enabled_boolean")
                .map(s -> Boolean.valueOf(s.getValue())).orElse(false);
    }

    @Transactional
    public void disableMailNotify() {
        settingRepository.findByKey("mail_enabled_boolean").ifPresent(s -> s.setValue(String.valueOf(false)));
    }

    public List<SettingObjDTO> getMailSettingsListWithDisplayName() {
        return settingRepository.findAll()
                .stream()
                .filter(s -> s.getKey().startsWith("mail"))
                .map(s -> {
                            if (s.getKey().contains("password"))
                                return new SettingObjDTO(s.getKey(), s.getDisplayName(), null);
                            return new SettingObjDTO(s.getKey(), s.getDisplayName(), s.getValue());
                        }
                )
                .collect(Collectors.toList());
    }

    public Map<String, String> getCompanySettingsMap() {
        return settingRepository.findAll()
                .stream()
                .filter(s -> s.getKey().startsWith("company"))
                .collect(HashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), Map::putAll);
    }

    public Setting save(Setting setting) {
        Setting editedSetting = settingRepository.findByKey(setting.getKey()).orElseThrow(() -> new ResourceNotFoundException("Setting not found"));
        editedSetting.setValue(setting.getValue());
        return settingRepository.save(editedSetting);
    }

    public void updateSettings(Map<String, String> newSettings) {
        if (newSettings.isEmpty()) return;
        settingRepository.findAll().forEach(s -> {
            if (newSettings.containsKey(s.getKey()) && !Objects.equals(s.getValue(), newSettings.get(s.getKey()))) {
                s.setValue(newSettings.get(s.getKey()));
                settingRepository.save(s);
            }
        });
    }

    public void checkJsonFormat(MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] decodedBytes = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        mapper.readTree(inputStream);
    }

    public String saveFile(MultipartFile file) throws IOException {
        byte[] decodedBytes = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        String fileName = file.getOriginalFilename();
        Path destinationFile = Paths.get(MvcConfiguration.DATA_DIR, fileName);
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

}
