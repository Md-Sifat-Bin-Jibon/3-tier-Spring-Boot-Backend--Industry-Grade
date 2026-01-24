package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Job application response DTO")
public class ApplicationResponse {

    @Schema(description = "Application ID")
    private Long id;

    @Schema(description = "Job ID")
    private Long jobId;

    @Schema(description = "Job title")
    private String jobTitle;

    @Schema(description = "Company name")
    private String company;

    @Schema(description = "Location")
    private String location;

    @Schema(description = "Job type")
    private String jobType;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Applied date")
    private LocalDateTime appliedDate;

    @Schema(description = "Salary")
    private String salary;

    @Schema(description = "Career track")
    private String careerTrack;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCareerTrack() {
        return careerTrack;
    }

    public void setCareerTrack(String careerTrack) {
        this.careerTrack = careerTrack;
    }
}
