package pl.mgis.problemreport.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ReportDTO {
    private long id;
    private String uuid;
    private LocalDateTime created;
    private double lat;
    private double lon;
    private String reportStatus;
    private ReportStatusDTO reportStatusDTO;
    @NotBlank(message = "Email field cannot be empty")
    @Email(message = "Provide valid email address",regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;
    @NotBlank(message = "This field cannot be empty")
    private String message;
    private String photo;
    private String photoPath;
    private ReportTypeDTO reportTypeDTO;
    private String ReportType;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public ReportStatusDTO getReportStatusDTO() {
        return reportStatusDTO;
    }

    public void setReportStatusDTO(ReportStatusDTO reportStatusDTO) {
        this.reportStatusDTO = reportStatusDTO;
        this.reportStatus = reportStatusDTO.getDescription();

    }

    public String getReportStatus() {
        return reportStatus;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ReportTypeDTO getreportTypeDTO() {
        return reportTypeDTO;
    }

    public void setReportTypeDTO(ReportTypeDTO reportTypeDTO) {
        this.reportTypeDTO = reportTypeDTO;
        this.ReportType = reportTypeDTO.getDescription();
    }

    public String getReportType() {
        return ReportType;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
