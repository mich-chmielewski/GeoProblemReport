package pl.mgis.problemreport.model.dto;

import javax.validation.constraints.NotBlank;

public class ReportTypeDTO {

    private long id;
    private boolean active;
    @NotBlank(message = "This field cannot be blank")
    private String description;
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
