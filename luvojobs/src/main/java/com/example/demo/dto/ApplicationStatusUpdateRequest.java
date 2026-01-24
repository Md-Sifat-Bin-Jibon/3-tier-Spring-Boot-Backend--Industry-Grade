package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Application status update request DTO")
public class ApplicationStatusUpdateRequest {

    @Schema(description = "New status", example = "shortlisted", required = true, allowableValues = {"pending", "reviewing", "shortlisted", "rejected", "hired"})
    @NotBlank(message = "Status is required")
    private String status;

    @Schema(description = "Optional notes for the status update")
    private String notes;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
