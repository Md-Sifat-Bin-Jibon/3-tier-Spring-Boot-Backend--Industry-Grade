package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Candidate profile request DTO")
public class CandidateProfileRequest {

    @Schema(description = "Full name", example = "John Doe")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Schema(description = "Education level", example = "Bachelor's Degree")
    @NotBlank(message = "Education level is required")
    private String educationLevel;

    @Schema(description = "Experience level", example = "Entry Level (0-1 years)")
    @NotBlank(message = "Experience level is required")
    private String experienceLevel;

    @Schema(description = "Preferred career track", example = "Web Development")
    @NotBlank(message = "Preferred career track is required")
    private String preferredCareerTrack;

    @Schema(description = "Target role", example = "Frontend Developer")
    @NotBlank(message = "Target role is required")
    private String targetRole;

    @Schema(description = "List of skills")
    @Valid
    private List<SkillRequest> skills;

    @Schema(description = "List of projects")
    @Valid
    private List<ProjectRequest> projects;

    @Schema(description = "List of work experiences")
    @Valid
    private List<WorkExperienceRequest> experiences;

    @Schema(description = "List of education entries")
    @Valid
    private List<EducationRequest> educations;

    // Getters and Setters
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

    public List<SkillRequest> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillRequest> skills) {
        this.skills = skills;
    }

    public List<ProjectRequest> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectRequest> projects) {
        this.projects = projects;
    }

    public List<WorkExperienceRequest> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<WorkExperienceRequest> experiences) {
        this.experiences = experiences;
    }

    public List<EducationRequest> getEducations() {
        return educations;
    }

    public void setEducations(List<EducationRequest> educations) {
        this.educations = educations;
    }

    // Nested DTOs
    @Schema(description = "Skill request")
    public static class SkillRequest {
        @Schema(description = "Skill name", example = "JavaScript")
        @NotBlank(message = "Skill name is required")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Schema(description = "Project request")
    public static class ProjectRequest {
        @Schema(description = "Project title", example = "E-commerce Website")
        @NotBlank(message = "Project title is required")
        private String title;

        @Schema(description = "Project description")
        private String description;

        @Schema(description = "Technologies used", example = "React, Node.js, MongoDB")
        private String technologies;

        @Schema(description = "Project link", example = "https://github.com/user/project")
        private String link;

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

    @Schema(description = "Work experience request")
    public static class WorkExperienceRequest {
        @Schema(description = "Company name", example = "Tech Corp")
        @NotBlank(message = "Company name is required")
        private String company;

        @Schema(description = "Position", example = "Software Engineer")
        @NotBlank(message = "Position is required")
        private String position;

        @Schema(description = "Start date", example = "2020-01-01")
        @NotBlank(message = "Start date is required")
        private String startDate;

        @Schema(description = "End date", example = "2022-12-31")
        private String endDate;

        @Schema(description = "Currently working here", example = "false")
        private Boolean current = false;

        @Schema(description = "Job description")
        private String description;

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

    @Schema(description = "Education request")
    public static class EducationRequest {
        @Schema(description = "Institution name", example = "University of Technology")
        @NotBlank(message = "Institution name is required")
        private String institution;

        @Schema(description = "Degree", example = "Bachelor of Science")
        @NotBlank(message = "Degree is required")
        private String degree;

        @Schema(description = "Field of study", example = "Computer Science")
        private String field;

        @Schema(description = "Start date", example = "2016-09-01")
        @NotBlank(message = "Start date is required")
        private String startDate;

        @Schema(description = "End date", example = "2020-06-30")
        private String endDate;

        @Schema(description = "Currently studying", example = "false")
        private Boolean current = false;

        @Schema(description = "GPA", example = "3.8")
        private String gpa;

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
