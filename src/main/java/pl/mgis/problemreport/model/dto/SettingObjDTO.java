package pl.mgis.problemreport.model.dto;

public class SettingObjDTO {

    private String key;
    private String displayName;
    private String value;

    public SettingObjDTO() {
    }

    public SettingObjDTO(String key, String displayName, String value) {
        this.key = key;
        this.displayName = displayName;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
