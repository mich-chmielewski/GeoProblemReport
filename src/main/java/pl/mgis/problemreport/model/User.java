package pl.mgis.problemreport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String username;
    @NotEmpty
    @DiffIgnore
    private String password;
    @Column(name = "enabled", columnDefinition = "boolean default false")
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Email
    private String email;
    @Embedded
    private Audit audit = new Audit();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
