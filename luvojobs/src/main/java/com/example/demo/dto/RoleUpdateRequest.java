package com.example.demo.dto;

import com.example.demo.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request DTO for updating user role")
public class RoleUpdateRequest {

    @Schema(description = "User role to set", example = "CANDIDATE", required = true)
    @NotNull(message = "Role is required")
    private UserRole role;

    // Constructors
    public RoleUpdateRequest() {}

    public RoleUpdateRequest(UserRole role) {
        this.role = role;
    }

    // Getters and Setters
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
