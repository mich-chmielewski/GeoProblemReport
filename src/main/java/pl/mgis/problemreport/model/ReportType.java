package pl.mgis.problemreport.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class ReportType extends BaseEntity {

    private String description;
    @Column(name = "active", columnDefinition = "boolean default false")
    private boolean active;
    private String email;
    @Embedded
    private Audit audit = new Audit();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
