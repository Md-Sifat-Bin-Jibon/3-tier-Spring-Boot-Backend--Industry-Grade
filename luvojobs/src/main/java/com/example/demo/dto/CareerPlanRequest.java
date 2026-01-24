package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Career plan creation/update request DTO")
public class CareerPlanRequest {

    @Schema(description = "Student ID", example = "1", required = true)
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @Schema(description = "Plan title", example = "Software Development Career Path", required = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Timeline", example = "6 months", required = true)
    @NotBlank(message = "Timeline is required")
    private String timeline;

    @Schema(description = "Goals list", required = true)
    @NotNull(message = "Goals are required")
    private List<String> goals;

    @Schema(description = "Action items list", required = true)
    @NotNull(message = "Action items are required")
    private List<String> actionItems;

    @Schema(description = "Status", example = "active")
    private String status;

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
    }

    public List<String> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<String> actionItems) {
        this.actionItems = actionItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
