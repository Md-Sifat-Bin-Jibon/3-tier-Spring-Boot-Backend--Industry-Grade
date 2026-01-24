package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Job creation/update request DTO")
public class JobRequest {

    @Schema(description = "Job title", example = "Senior Software Engineer", required = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Company name", example = "Tech Corp", required = true)
    @NotBlank(message = "Company is required")
    private String company;

    @Schema(description = "Location", example = "Remote, New York, NY", required = true)
    @NotBlank(message = "Location is required")
    private String location;

    @Schema(description = "Job type", example = "full-time", required = true)
    @NotBlank(message = "Type is required")
    private String type;

    @Schema(description = "Experience level", example = "senior", required = true)
    @NotBlank(message = "Experience level is required")
    private String experienceLevel;

    @Schema(description = "Job description", required = true)
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "Requirements list")
    private List<String> requirements;

    @Schema(description = "Required skills list")
    private List<String> requiredSkills;

    @Schema(description = "Salary range", example = "$50,000 - $70,000", required = true)
    @NotBlank(message = "Salary is required")
    private String salary;

    @Schema(description = "Career track", example = "software-development")
    private String careerTrack;

    @Schema(description = "Application deadline", example = "2024-12-31")
    private LocalDate deadline;

    @Schema(description = "Job status", example = "active")
    private String status;

    @Schema(description = "Fruvo coins required", example = "50")
    private Integer fruvoCoinRequired;

    // Getters and Setters
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
}
