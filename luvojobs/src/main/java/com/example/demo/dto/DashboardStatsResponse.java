package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dashboard statistics response DTO")
public class DashboardStatsResponse {

    @Schema(description = "Total applications")
    private Long totalApplications;

    @Schema(description = "Pending applications")
    private Long pendingApplications;

    @Schema(description = "Shortlisted applications")
    private Long shortlisted;

    @Schema(description = "Rejected applications")
    private Long rejected;

    @Schema(description = "Hired applications")
    private Long hired;

    @Schema(description = "Upcoming interviews")
    private Long upcomingInterviews;

    @Schema(description = "Saved jobs count")
    private Long savedJobs;

    @Schema(description = "Fruvo coins")
    private Integer coins;

    @Schema(description = "User score")
    private Integer score;

    // Getters and Setters
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

    public Long getRejected() {
        return rejected;
    }

    public void setRejected(Long rejected) {
        this.rejected = rejected;
    }

    public Long getHired() {
        return hired;
    }

    public void setHired(Long hired) {
        this.hired = hired;
    }

    public Long getUpcomingInterviews() {
        return upcomingInterviews;
    }

    public void setUpcomingInterviews(Long upcomingInterviews) {
        this.upcomingInterviews = upcomingInterviews;
    }

    public Long getSavedJobs() {
        return savedJobs;
    }

    public void setSavedJobs(Long savedJobs) {
        this.savedJobs = savedJobs;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
