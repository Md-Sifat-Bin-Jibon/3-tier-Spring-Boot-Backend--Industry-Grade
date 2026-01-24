package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Candidate profile response DTO")
public class CandidateProfileResponse {

    @Schema(description = "Profile ID")
    private Long id;

    @Schema(description = "Full name")
    private String fullName;

    @Schema(description = "Education level")
    private String educationLevel;

    @Schema(description = "Experience level")
    private String experienceLevel;

    @Schema(description = "Preferred career track")
    private String preferredCareerTrack;

    @Schema(description = "Target role")
    private String targetRole;

    @Schema(description = "CV file name")
    private String cvFileName;

    @Schema(description = "List of skills")
    private List<String> skills;

    @Schema(description = "List of projects")
    private List<ProjectResponse> projects;

    @Schema(description = "List of work experiences")
    private List<WorkExperienceResponse> experiences;

    @Schema(description = "List of education entries")
    private List<EducationResponse> educations;

    @Schema(description = "Created at")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getPreferredCareerTrack() {
        return preferredCareerTrack;
    }

    public void setPreferredCareerTrack(String preferredCareerTrack) {
        this.preferredCareerTrack = preferredCareerTrack;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getCvFileName() {
        return cvFileName;
    }

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<ProjectResponse> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectResponse> projects) {
        this.projects = projects;
    }

    public List<WorkExperienceResponse> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<WorkExperienceResponse> experiences) {
        this.experiences = experiences;
    }

    public List<EducationResponse> getEducations() {
        return educations;
    }

    public void setEducations(List<EducationResponse> educations) {
        this.educations = educations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Nested response DTOs
    @Schema(description = "Project response")
    public static class ProjectResponse {
        private Long id;
        private String title;
        private String description;
        private String technologies;
        private String link;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTechnologies() {
            return technologies;
        }

        public void setTechnologies(String technologies) {
            this.technologies = technologies;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    @Schema(description = "Work experience response")
    public static class WorkExperienceResponse {
        private Long id;
        private String company;
        private String position;
        private String startDate;
        private String endDate;
        private Boolean current;
        private String description;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Boolean getCurrent() {
            return current;
        }

        public void setCurrent(Boolean current) {
            this.current = current;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    @Schema(description = "Education response")
    public static class EducationResponse {
        private Long id;
        private String institution;
        private String degree;
        private String field;
        private String startDate;
        private String endDate;
        private Boolean current;
        private String gpa;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getInstitution() {
            return institution;
        }

        public void setInstitution(String institution) {
            this.institution = institution;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Boolean getCurrent() {
            return current;
        }

        public void setCurrent(Boolean current) {
            this.current = current;
        }

        public String getGpa() {
            return gpa;
        }

        public void setGpa(String gpa) {
            this.gpa = gpa;
        }
    }
}
