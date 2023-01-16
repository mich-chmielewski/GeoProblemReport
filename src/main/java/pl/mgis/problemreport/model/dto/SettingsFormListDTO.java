package pl.mgis.problemreport.model.dto;

import java.util.List;

public class SettingsFormListDTO {

    List<SettingObjDTO> settingObjDTOS;

    public SettingsFormListDTO() {
    }

    public SettingsFormListDTO(List<SettingObjDTO> settingObjDTOS) {
        this.settingObjDTOS = settingObjDTOS;
    }

    public List<SettingObjDTO> getSettingObjDTOS() {
        return settingObjDTOS;
    }

    public void setSettingObjDTOS(List<SettingObjDTO> settingObjDTOS) {
        this.settingObjDTOS = settingObjDTOS;
    }

    public void addSettingObjDTOS(SettingObjDTO settingObjDTO) {
        settingObjDTOS.add(settingObjDTO);
    }
}
