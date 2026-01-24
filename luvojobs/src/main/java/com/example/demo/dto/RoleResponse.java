package com.example.demo.dto;

import com.example.demo.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for role operations")
public class RoleResponse {

    @Schema(description = "Success status", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "Role updated successfully")
    private String message;

    @Schema(description = "Current user role", example = "CANDIDATE")
    private UserRole role;

    @Schema(description = "User email", example = "user@example.com")
    private String email;

    @Schema(description = "User username", example = "a1b2c3d4e5f6g7h8")
    private String username;

    // Constructors
    public RoleResponse() {}

    public RoleResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RoleResponse(boolean success, String message, UserRole role, String email, String username) {
        this.success = success;
        this.message = message;
        this.role = role;
        this.email = email;
        this.username = username;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
