package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/career-counselor")
@Tag(name = "Career Counselor Management", description = "API endpoints for career counselor operations - student management, counseling sessions, career plans, and resources")
public class CareerCounselorController {

    private final StudentService studentService;
    private final CounselingSessionService sessionService;
    private final CareerPlanService careerPlanService;
    private final ResourceService resourceService;
    private final CounselorDashboardService dashboardService;
    private final com.example.demo.repository.UserRepository userRepository;

    public CareerCounselorController(StudentService studentService,
                                    CounselingSessionService sessionService,
                                    CareerPlanService careerPlanService,
                                    ResourceService resourceService,
                                    CounselorDashboardService dashboardService,
                                    com.example.demo.repository.UserRepository userRepository) {
        this.studentService = studentService;
        this.sessionService = sessionService;
        this.careerPlanService = careerPlanService;
        this.resourceService = resourceService;
        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    // Dashboard Endpoints
    @GetMapping("/dashboard/stats")
    @Operation(
        summary = "Get career counselor dashboard statistics",
        description = "Retrieves comprehensive dashboard statistics including student counts, session statistics, career plans, and resources"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Statistics retrieved successfully",
            content = @Content(schema = @Schema(implementation = CounselorDashboardStatsResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<CounselorDashboardStatsResponse>> getDashboardStats() {
        try {
            Long counselorId = getCurrentUserId();
            CounselorDashboardStatsResponse stats = dashboardService.getDashboardStats(counselorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Statistics retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Student Management Endpoints
    @GetMapping("/students")
    @Operation(
        summary = "Get all students",
        description = "Retrieves a list of all students managed by the authenticated career counselor."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Students retrieved successfully",
            content = @Content(schema = @Schema(implementation = StudentResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudents() {
        try {
            Long counselorId = getCurrentUserId();
            List<StudentResponse> students = studentService.getStudents(counselorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Students retrieved successfully", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/students/status/{status}")
    @Operation(
        summary = "Get students by status",
        description = "Retrieves students filtered by status (active, inactive)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Students retrieved successfully",
            content = @Content(schema = @Schema(implementation = StudentResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByStatus(
            @Parameter(description = "Student status", example = "active", required = true) @PathVariable String status) {
        try {
            Long counselorId = getCurrentUserId();
            List<StudentResponse> students = studentService.getStudentsByStatus(counselorId, status);
            return ResponseEntity.ok(new ApiResponse<>(true, "Students retrieved successfully", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/students/{studentId}")
    @Operation(
        summary = "Get student by ID",
        description = "Retrieves detailed information about a specific student including their profile, session count, and career plan status."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Student retrieved successfully",
            content = @Content(schema = @Schema(implementation = StudentResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to view this student"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Student not found"
        )
    })
    public ResponseEntity<ApiResponse<StudentResponse>> getStudent(
            @Parameter(description = "Student ID", example = "1", required = true) @PathVariable Long studentId) {
        try {
            Long counselorId = getCurrentUserId();
            StudentResponse student = studentService.getStudentById(counselorId, studentId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Student retrieved successfully", student));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Counseling Session Endpoints
    @PostMapping("/sessions")
    @Operation(
        summary = "Schedule a counseling session",
        description = "Schedules a new counseling session for a student. Supports in-person, video, and phone session types."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Session scheduled successfully",
            content = @Content(schema = @Schema(implementation = SessionResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - validation failed or invalid data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Student not found"
        )
    })
    public ResponseEntity<ApiResponse<SessionResponse>> createSession(@Valid @RequestBody SessionRequest request) {
        try {
            Long counselorId = getCurrentUserId();
            SessionResponse response = sessionService.createSession(counselorId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Session scheduled successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/sessions/{sessionId}")
    @Operation(
        summary = "Update a counseling session",
        description = "Updates an existing counseling session. Only the counselor who scheduled it can update it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Session updated successfully",
            content = @Content(schema = @Schema(implementation = SessionResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - validation failed or invalid data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to update this session"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Session not found"
        )
    })
    public ResponseEntity<ApiResponse<SessionResponse>> updateSession(
            @Parameter(description = "Session ID", example = "1", required = true) @PathVariable Long sessionId,
            @Valid @RequestBody SessionRequest request) {
        try {
            Long counselorId = getCurrentUserId();
            SessionResponse response = sessionService.updateSession(counselorId, sessionId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Session updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/sessions/{sessionId}")
    @Operation(
        summary = "Delete a counseling session",
        description = "Deletes a counseling session. Only the counselor who scheduled it can delete it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Session deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to delete this session"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Session not found"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteSession(
            @Parameter(description = "Session ID", example = "1", required = true) @PathVariable Long sessionId) {
        try {
            Long counselorId = getCurrentUserId();
            sessionService.deleteSession(counselorId, sessionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Session deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/sessions")
    @Operation(
        summary = "Get all counseling sessions",
        description = "Retrieves all counseling sessions scheduled by the authenticated career counselor."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Sessions retrieved successfully",
            content = @Content(schema = @Schema(implementation = SessionResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessions() {
        try {
            Long counselorId = getCurrentUserId();
            List<SessionResponse> sessions = sessionService.getSessions(counselorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Sessions retrieved successfully", sessions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/sessions/status/{status}")
    @Operation(
        summary = "Get sessions by status",
        description = "Retrieves counseling sessions filtered by status (scheduled, completed, cancelled)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Sessions retrieved successfully",
            content = @Content(schema = @Schema(implementation = SessionResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessionsByStatus(
            @Parameter(description = "Session status", example = "scheduled", required = true) @PathVariable String status) {
        try {
            Long counselorId = getCurrentUserId();
            List<SessionResponse> sessions = sessionService.getSessionsByStatus(counselorId, status);
            return ResponseEntity.ok(new ApiResponse<>(true, "Sessions retrieved successfully", sessions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/sessions/{sessionId}/status")
    @Operation(
        summary = "Update session status",
        description = "Updates the status of a counseling session and optionally adds feedback."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Session status updated successfully",
            content = @Content(schema = @Schema(implementation = SessionResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - invalid status"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to update this session"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Session not found"
        )
    })
    public ResponseEntity<ApiResponse<SessionResponse>> updateSessionStatus(
            @Parameter(description = "Session ID", example = "1", required = true) @PathVariable Long sessionId,
            @Parameter(description = "New status", example = "completed", required = true) @RequestParam String status,
            @Parameter(description = "Optional feedback") @RequestParam(required = false) String feedback) {
        try {
            Long counselorId = getCurrentUserId();
            SessionResponse response = sessionService.updateSessionStatus(counselorId, sessionId, status, feedback);
            return ResponseEntity.ok(new ApiResponse<>(true, "Session status updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Career Plan Endpoints
    @PostMapping("/career-plans")
    @Operation(
        summary = "Create a career plan",
        description = "Creates a new career development plan for a student with goals, timeline, and action items."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Career plan created successfully",
            content = @Content(schema = @Schema(implementation = CareerPlanResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - validation failed or invalid data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Student not found"
        )
    })
    public ResponseEntity<ApiResponse<CareerPlanResponse>> createCareerPlan(@Valid @RequestBody CareerPlanRequest request) {
        try {
            Long counselorId = getCurrentUserId();
            CareerPlanResponse response = careerPlanService.createCareerPlan(counselorId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career plan created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/career-plans/{planId}")
    @Operation(
        summary = "Update a career plan",
        description = "Updates an existing career plan. Only the counselor who created it can update it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Career plan updated successfully",
            content = @Content(schema = @Schema(implementation = CareerPlanResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - validation failed or invalid data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to update this career plan"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Career plan not found"
        )
    })
    public ResponseEntity<ApiResponse<CareerPlanResponse>> updateCareerPlan(
            @Parameter(description = "Career plan ID", example = "1", required = true) @PathVariable Long planId,
            @Valid @RequestBody CareerPlanRequest request) {
        try {
            Long counselorId = getCurrentUserId();
            CareerPlanResponse response = careerPlanService.updateCareerPlan(counselorId, planId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career plan updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/career-plans/{planId}")
    @Operation(
        summary = "Delete a career plan",
        description = "Deletes a career plan. Only the counselor who created it can delete it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Career plan deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to delete this career plan"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Career plan not found"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteCareerPlan(
            @Parameter(description = "Career plan ID", example = "1", required = true) @PathVariable Long planId) {
        try {
            Long counselorId = getCurrentUserId();
            careerPlanService.deleteCareerPlan(counselorId, planId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career plan deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/career-plans")
    @Operation(
        summary = "Get all career plans",
        description = "Retrieves all career plans created by the authenticated career counselor."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Career plans retrieved successfully",
            content = @Content(schema = @Schema(implementation = CareerPlanResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<CareerPlanResponse>>> getCareerPlans() {
        try {
            Long counselorId = getCurrentUserId();
            List<CareerPlanResponse> plans = careerPlanService.getCareerPlans(counselorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career plans retrieved successfully", plans));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/career-plans/status/{status}")
    @Operation(
        summary = "Get career plans by status",
        description = "Retrieves career plans filtered by status (draft, active, completed)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Career plans retrieved successfully",
            content = @Content(schema = @Schema(implementation = CareerPlanResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<CareerPlanResponse>>> getCareerPlansByStatus(
            @Parameter(description = "Career plan status", example = "active", required = true) @PathVariable String status) {
        try {
            Long counselorId = getCurrentUserId();
            List<CareerPlanResponse> plans = careerPlanService.getCareerPlansByStatus(counselorId, status);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career plans retrieved successfully", plans));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/career-plans/{planId}")
    @Operation(
        summary = "Get career plan by ID",
        description = "Retrieves detailed information about a specific career plan."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Career plan retrieved successfully",
            content = @Content(schema = @Schema(implementation = CareerPlanResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to view this career plan"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Career plan not found"
        )
    })
    public ResponseEntity<ApiResponse<CareerPlanResponse>> getCareerPlan(
            @Parameter(description = "Career plan ID", example = "1", required = true) @PathVariable Long planId) {
        try {
            Long counselorId = getCurrentUserId();
            CareerPlanResponse plan = careerPlanService.getCareerPlanById(counselorId, planId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Career plan retrieved successfully", plan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Resource Endpoints
    @PostMapping("/resources")
    @Operation(
        summary = "Create a resource",
        description = "Creates a new educational resource (article, video, course, book, tool, etc.) for students."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Resource created successfully",
            content = @Content(schema = @Schema(implementation = ResourceResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - validation failed or invalid data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<ResourceResponse>> createResource(@Valid @RequestBody ResourceRequest request) {
        try {
            Long counselorId = getCurrentUserId();
            ResourceResponse response = resourceService.createResource(counselorId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resource created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/resources/{resourceId}")
    @Operation(
        summary = "Update a resource",
        description = "Updates an existing resource. Only the counselor who created it can update it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Resource updated successfully",
            content = @Content(schema = @Schema(implementation = ResourceResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - validation failed or invalid data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to update this resource"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Resource not found"
        )
    })
    public ResponseEntity<ApiResponse<ResourceResponse>> updateResource(
            @Parameter(description = "Resource ID", example = "1", required = true) @PathVariable Long resourceId,
            @Valid @RequestBody ResourceRequest request) {
        try {
            Long counselorId = getCurrentUserId();
            ResourceResponse response = resourceService.updateResource(counselorId, resourceId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resource updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/resources/{resourceId}")
    @Operation(
        summary = "Delete a resource",
        description = "Deletes a resource. Only the counselor who created it can delete it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Resource deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to delete this resource"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Resource not found"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteResource(
            @Parameter(description = "Resource ID", example = "1", required = true) @PathVariable Long resourceId) {
        try {
            Long counselorId = getCurrentUserId();
            resourceService.deleteResource(counselorId, resourceId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resource deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/resources")
    @Operation(
        summary = "Get all resources",
        description = "Retrieves all resources created by the authenticated career counselor."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Resources retrieved successfully",
            content = @Content(schema = @Schema(implementation = ResourceResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getResources() {
        try {
            Long counselorId = getCurrentUserId();
            List<ResourceResponse> resources = resourceService.getResources(counselorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resources retrieved successfully", resources));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/resources/type/{type}")
    @Operation(
        summary = "Get resources by type",
        description = "Retrieves resources filtered by type (article, video, course, book, tool, other)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Resources retrieved successfully",
            content = @Content(schema = @Schema(implementation = ResourceResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getResourcesByType(
            @Parameter(description = "Resource type", example = "article", required = true) @PathVariable String type) {
        try {
            Long counselorId = getCurrentUserId();
            List<ResourceResponse> resources = resourceService.getResourcesByType(counselorId, type);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resources retrieved successfully", resources));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/resources/featured")
    @Operation(
        summary = "Get featured resources",
        description = "Retrieves all featured resources created by the authenticated career counselor."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Featured resources retrieved successfully",
            content = @Content(schema = @Schema(implementation = ResourceResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getFeaturedResources() {
        try {
            Long counselorId = getCurrentUserId();
            List<ResourceResponse> resources = resourceService.getFeaturedResources(counselorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Featured resources retrieved successfully", resources));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/resources/{resourceId}")
    @Operation(
        summary = "Get resource by ID",
        description = "Retrieves detailed information about a specific resource."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Resource retrieved successfully",
            content = @Content(schema = @Schema(implementation = ResourceResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to view this resource"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Resource not found"
        )
    })
    public ResponseEntity<ApiResponse<ResourceResponse>> getResource(
            @Parameter(description = "Resource ID", example = "1", required = true) @PathVariable Long resourceId) {
        try {
            Long counselorId = getCurrentUserId();
            ResourceResponse resource = resourceService.getResourceById(counselorId, resourceId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resource retrieved successfully", resource));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
