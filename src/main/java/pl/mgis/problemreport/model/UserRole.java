package pl.mgis.problemreport.model;

public enum UserRole {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String asRole() {
        return role;
    }
}
