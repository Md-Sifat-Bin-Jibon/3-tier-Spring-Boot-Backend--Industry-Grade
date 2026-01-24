package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Interview creation/update request DTO")
public class InterviewRequest {

    @Schema(description = "Application ID", example = "1", required = true)
    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @Schema(description = "Scheduled date and time", example = "2024-01-20T10:00:00", required = true)
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    @Schema(description = "Duration in minutes", example = "60", required = true)
    @NotNull(message = "Duration is required")
    private Integer durationMinutes;

    @Schema(description = "Interview type", example = "video", required = true, allowableValues = {"in-person", "video", "phone"})
    @NotBlank(message = "Type is required")
    private String type;

    @Schema(description = "Meeting link (for video interviews)")
    private String meetingLink;

    @Schema(description = "Location (for in-person interviews)")
    private String location;

    @Schema(description = "Notes")
    private String notes;

    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
