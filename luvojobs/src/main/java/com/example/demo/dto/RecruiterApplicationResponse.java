package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Recruiter application response DTO with candidate details")
public class RecruiterApplicationResponse {

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

    @Schema(description = "Candidate ID")
    private Long candidateId;

    @Schema(description = "Candidate name")
    private String candidateName;

    @Schema(description = "Candidate email")
    private String candidateEmail;

    @Schema(description = "Candidate phone")
    private String candidatePhone;

    @Schema(description = "Candidate experience (years)")
    private String experience;

    @Schema(description = "Candidate education")
    private String education;

    @Schema(description = "Candidate skills")
    private java.util.List<String> skills;

    @Schema(description = "Cover letter")
    private String coverLetter;

    @Schema(description = "Notes")
    private String notes;

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

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCandidatePhone() {
        return candidatePhone;
    }

    public void setCandidatePhone(String candidatePhone) {
        this.candidatePhone = candidatePhone;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public java.util.List<String> getSkills() {
        return skills;
    }

    public void setSkills(java.util.List<String> skills) {
        this.skills = skills;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
