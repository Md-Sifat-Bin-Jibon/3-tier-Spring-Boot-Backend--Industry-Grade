package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Job response DTO")
public class JobResponse {

    @Schema(description = "Job ID")
    private Long id;

    @Schema(description = "Job title")
    private String title;

    @Schema(description = "Company name")
    private String company;

    @Schema(description = "Location")
    private String location;

    @Schema(description = "Job type")
    private String type;

    @Schema(description = "Experience level")
    private String experienceLevel;

    @Schema(description = "Job description")
    private String description;

    @Schema(description = "Requirements")
    private List<String> requirements;

    @Schema(description = "Required skills")
    private List<String> requiredSkills;

    @Schema(description = "Salary")
    private String salary;

    @Schema(description = "Career track")
    private String careerTrack;

    @Schema(description = "Posted date")
    private LocalDate postedDate;

    @Schema(description = "Deadline")
    private LocalDate deadline;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Fruvo coins required")
    private Integer fruvoCoinRequired;

    @Schema(description = "Views count")
    private Integer views;

    @Schema(description = "Match score (for candidate matching)")
    private Integer matchScore;

    @Schema(description = "Match reasons (for candidate matching)")
    private List<String> matchReasons;

    @Schema(description = "Matched skills (for candidate matching)")
    private List<String> matchedSkills;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
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

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFruvoCoinRequired() {
        return fruvoCoinRequired;
    }

    public void setFruvoCoinRequired(Integer fruvoCoinRequired) {
        this.fruvoCoinRequired = fruvoCoinRequired;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public List<String> getMatchReasons() {
        return matchReasons;
    }

    public void setMatchReasons(List<String> matchReasons) {
        this.matchReasons = matchReasons;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }
}
