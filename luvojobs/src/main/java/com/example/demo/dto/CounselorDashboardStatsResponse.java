package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Career counselor dashboard statistics response DTO")
public class CounselorDashboardStatsResponse {

    @Schema(description = "Active students")
    private Long activeStudents;

    @Schema(description = "Total students")
    private Long totalStudents;

    @Schema(description = "Scheduled sessions")
    private Long scheduledSessions;

    @Schema(description = "Total sessions")
    private Long totalSessions;

    @Schema(description = "Active career plans")
    private Long activeCareerPlans;

    @Schema(description = "Total career plans")
    private Long totalCareerPlans;

    @Schema(description = "Total resources")
    private Long totalResources;

    @Schema(description = "Featured resources")
    private Long featuredResources;

    // Getters and Setters
    public Long getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(Long activeStudents) {
        this.activeStudents = activeStudents;
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Long getScheduledSessions() {
        return scheduledSessions;
    }

    public void setScheduledSessions(Long scheduledSessions) {
        this.scheduledSessions = scheduledSessions;
    }

    public Long getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(Long totalSessions) {
        this.totalSessions = totalSessions;
    }

    public Long getActiveCareerPlans() {
        return activeCareerPlans;
    }

    public void setActiveCareerPlans(Long activeCareerPlans) {
        this.activeCareerPlans = activeCareerPlans;
    }

    public Long getTotalCareerPlans() {
        return totalCareerPlans;
    }

    public void setTotalCareerPlans(Long totalCareerPlans) {
        this.totalCareerPlans = totalCareerPlans;
    }

    public Long getTotalResources() {
        return totalResources;
    }

    public void setTotalResources(Long totalResources) {
        this.totalResources = totalResources;
    }

    public Long getFeaturedResources() {
        return featuredResources;
    }

    public void setFeaturedResources(Long featuredResources) {
        this.featuredResources = featuredResources;
    }
}
