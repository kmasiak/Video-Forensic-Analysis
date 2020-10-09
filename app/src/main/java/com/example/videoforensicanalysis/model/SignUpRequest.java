package com.example.videoforensicanalysis.model;

public class SignUpRequest {
    String name, username, email, password, gender;
    Boolean isLegalAge;

    public SignUpRequest() {
    }

    public SignUpRequest(String name, String username, String email, String password, String gender, Boolean isLegalAge) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.isLegalAge = isLegalAge;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getLegalAge() {
        return isLegalAge;
    }

    public void setLegalAge(Boolean legalAge) {
        isLegalAge = legalAge;
    }
}
