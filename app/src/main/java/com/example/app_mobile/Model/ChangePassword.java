package com.example.app_mobile.Model;

public class ChangePassword {
    private String email;
    private String password;

    public ChangePassword(){}

    public ChangePassword(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ChangePassword{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
