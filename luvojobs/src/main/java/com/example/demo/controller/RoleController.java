package com.example.demo.controller;

import com.example.demo.dto.RoleResponse;
import com.example.demo.dto.RoleUpdateRequest;
import com.example.demo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
@Tag(name = "Role Management", description = "API endpoints for managing user roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/current")
    @Operation(
        summary = "Get Current User Role",
        description = "Retrieve the current role of the authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Role retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<RoleResponse> getCurrentRole(Authentication authentication) {
        String email = authentication.getName();
        RoleResponse response = roleService.getCurrentRole(email);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/update")
    @Operation(
        summary = "Update User Role",
        description = "Update the role of the authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Role updated successfully",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - invalid role or validation failed",
            content = @Content(schema = @Schema(implementation = RoleResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<RoleResponse> updateRole(
            @Valid @RequestBody RoleUpdateRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        RoleResponse response = roleService.updateRole(email, request.getRole());
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
