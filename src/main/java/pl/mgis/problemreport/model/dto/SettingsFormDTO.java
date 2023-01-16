package pl.mgis.problemreport.model.dto;

import java.util.Map;

public class SettingsFormDTO {

    public Map<String, String> settings;

    public SettingsFormDTO() {
    }

    public SettingsFormDTO(Map<String, String> settings) {
        this.settings = settings;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    public void addSetting(String key, String value) {
        settings.put(key, value);
    }
}
