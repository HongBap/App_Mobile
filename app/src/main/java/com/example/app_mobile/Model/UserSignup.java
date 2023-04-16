package com.example.app_mobile.Model;

public class UserSignup {
    private int id;
    private String username;
    private String email;
    private String role;
    private String password;

    public UserSignup() {
        role = "ROLE_USER";
    }

    public UserSignup(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = "ROLE_USER";
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserSignup{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
