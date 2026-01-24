package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Recruiter dashboard statistics response DTO")
public class RecruiterDashboardStatsResponse {

    @Schema(description = "Total active jobs")
    private Long activeJobs;

    @Schema(description = "Total jobs posted")
    private Long totalJobs;

    @Schema(description = "Total applications")
    private Long totalApplications;

    @Schema(description = "Pending applications")
    private Long pendingApplications;

    @Schema(description = "Shortlisted applications")
    private Long shortlisted;

    @Schema(description = "Hired applications")
    private Long hired;

    @Schema(description = "Rejected applications")
    private Long rejected;

    @Schema(description = "Total candidates")
    private Long totalCandidates;

    @Schema(description = "Upcoming interviews")
    private Long upcomingInterviews;

    // Getters and Setters
    public Long getActiveJobs() {
        return activeJobs;
    }

    public void setActiveJobs(Long activeJobs) {
        this.activeJobs = activeJobs;
    }

    public Long getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(Long totalJobs) {
        this.totalJobs = totalJobs;
    }

    public Long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Long getPendingApplications() {
        return pendingApplications;
    }

    public void setPendingApplications(Long pendingApplications) {
        this.pendingApplications = pendingApplications;
    }

    public Long getShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(Long shortlisted) {
        this.shortlisted = shortlisted;
    }

    public Long getHired() {
        return hired;
    }

    public void setHired(Long hired) {
        this.hired = hired;
    }

    public Long getRejected() {
        return rejected;
    }

    public void setRejected(Long rejected) {
        this.rejected = rejected;
    }

    public Long getTotalCandidates() {
        return totalCandidates;
    }

    public void setTotalCandidates(Long totalCandidates) {
        this.totalCandidates = totalCandidates;
    }

    public Long getUpcomingInterviews() {
        return upcomingInterviews;
    }

    public void setUpcomingInterviews(Long upcomingInterviews) {
        this.upcomingInterviews = upcomingInterviews;
    }
}
