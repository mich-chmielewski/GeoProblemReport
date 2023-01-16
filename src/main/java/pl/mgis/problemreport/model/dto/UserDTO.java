package pl.mgis.problemreport.model.dto;

import pl.mgis.problemreport.model.UserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDTO {
    private long id;
    @NotEmpty(message = "Username field cannot be empty")
    private String username;
    @NotEmpty(message = "Password field cannot be empty")
    private String password;
    @NotNull(message = "User active field cannot be null")
    private boolean enabled;
    @NotNull(message = "Only ROLE_USER or ROLE_ADMIN allowed")
    private UserRole userRole;
    @Email(message = "Please provide valid email address")
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
