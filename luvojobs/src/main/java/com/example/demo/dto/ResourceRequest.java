package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Resource creation/update request DTO")
public class ResourceRequest {

    @Schema(description = "Resource title", example = "Introduction to Software Engineering", required = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Description", required = true)
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "Resource type", example = "article", required = true, allowableValues = {"article", "video", "course", "book", "tool", "other"})
    @NotBlank(message = "Type is required")
    private String type;

    @Schema(description = "Resource URL", example = "https://example.com/resource", required = true)
    @NotBlank(message = "Resource URL is required")
    private String resourceUrl;

    @Schema(description = "Category", example = "career-development")
    private String category;

    @Schema(description = "Is featured", example = "false")
    private Boolean isFeatured;

    // Getters and Setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
}
