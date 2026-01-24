package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Candidate response DTO for recruiters")
public class CandidateResponse {

    @Schema(description = "Candidate user ID")
    private Long userId;

    @Schema(description = "Candidate name")
    private String name;

    @Schema(description = "Candidate email")
    private String email;

    @Schema(description = "Candidate phone")
    private String phone;

    @Schema(description = "Career track")
    private String careerTrack;

    @Schema(description = "Experience level")
    private String experienceLevel;

    @Schema(description = "Skills")
    private List<String> skills;

    @Schema(description = "Total applications")
    private Long totalApplications;

    @Schema(description = "Shortlisted applications")
    private Long shortlistedApplications;

    @Schema(description = "Hired applications")
    private Long hiredApplications;

    // Getters and Setters
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

    public String getCareerTrack() {
        return careerTrack;
    }

    public void setCareerTrack(String careerTrack) {
        this.careerTrack = careerTrack;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public Long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Long getShortlistedApplications() {
        return shortlistedApplications;
    }

    public void setShortlistedApplications(Long shortlistedApplications) {
        this.shortlistedApplications = shortlistedApplications;
    }

    public Long getHiredApplications() {
        return hiredApplications;
    }

    public void setHiredApplications(Long hiredApplications) {
        this.hiredApplications = hiredApplications;
    }
}
