package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Student response DTO")
public class StudentResponse {

    @Schema(description = "Student ID")
    private Long id;

    @Schema(description = "User ID")
    private Long userId;

    @Schema(description = "Student name")
    private String name;

    @Schema(description = "Student email")
    private String email;

    @Schema(description = "Student phone")
    private String phone;

    @Schema(description = "Program")
    private String program;

    @Schema(description = "Year")
    private String year;

    @Schema(description = "GPA")
    private String gpa;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Last session date")
    private LocalDateTime lastSessionDate;

    @Schema(description = "Total sessions count")
    private Long sessionsCount;

    @Schema(description = "Has career plan")
    private Boolean hasCareerPlan;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastSessionDate() {
        return lastSessionDate;
    }

    public void setLastSessionDate(LocalDateTime lastSessionDate) {
        this.lastSessionDate = lastSessionDate;
    }

    public Long getSessionsCount() {
        return sessionsCount;
    }

    public void setSessionsCount(Long sessionsCount) {
        this.sessionsCount = sessionsCount;
    }

    public Boolean getHasCareerPlan() {
        return hasCareerPlan;
    }

    public void setHasCareerPlan(Boolean hasCareerPlan) {
        this.hasCareerPlan = hasCareerPlan;
    }
}
