package pl.mgis.problemreport.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ReportStatus extends BaseEntity {

    private String description;
    private long statusCode;
    private String color;
    @Enumerated(EnumType.STRING)
    private Stage stage;
    @Embedded
    private Audit audit = new Audit();

    public ReportStatus() {
    }

    public ReportStatus(String description, int statusCode, String color, Stage stage) {
        this.description = description;
        this.statusCode = statusCode;
        this.color = color;
        this.stage = stage;
    }

    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
