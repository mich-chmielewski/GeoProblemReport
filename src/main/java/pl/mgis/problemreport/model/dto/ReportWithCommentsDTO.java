package pl.mgis.problemreport.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class ReportWithCommentsDTO {

    private long id;
    private LocalDateTime created;
    private double lat;
    private double lon;
    private ReportStatusDTO reportStatusDTO;
    private String email;
    private String message;
    private String photoPath;
    @JsonProperty("reportType")
    private ReportTypeDTO reportTypeDTO;
    private List<CommentDTO> comments;

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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public ReportTypeDTO getreportTypeDTO() {
        return reportTypeDTO;
    }

    public void setreportTypeDTO(ReportTypeDTO reportTypeDTO) {
        this.reportTypeDTO = reportTypeDTO;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
