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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@Tag(name = "Recruiter Management", description = "API endpoints for recruiter operations - job posting, application management, interviews, and analytics")
public class RecruiterController {

    private final RecruiterJobService jobService;
    private final RecruiterApplicationService applicationService;
    private final InterviewService interviewService;
    private final CandidateService candidateService;
    private final RecruiterDashboardService dashboardService;
    private final com.example.demo.repository.UserRepository userRepository;

    public RecruiterController(RecruiterJobService jobService,
                               RecruiterApplicationService applicationService,
                               InterviewService interviewService,
                               CandidateService candidateService,
                               RecruiterDashboardService dashboardService,
                               com.example.demo.repository.UserRepository userRepository) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
        this.candidateService = candidateService;
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
        summary = "Get recruiter dashboard statistics",
        description = "Retrieves comprehensive dashboard statistics including job counts, application statistics, candidate counts, and upcoming interviews"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Statistics retrieved successfully",
            content = @Content(schema = @Schema(implementation = RecruiterDashboardStatsResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<RecruiterDashboardStatsResponse>> getDashboardStats() {
        try {
            Long recruiterId = getCurrentUserId();
            RecruiterDashboardStatsResponse stats = dashboardService.getDashboardStats(recruiterId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Statistics retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Job Management Endpoints
    @PostMapping("/jobs")
    @Operation(
        summary = "Create a new job posting",
        description = "Creates a new job posting. The job will be associated with the authenticated recruiter."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Job created successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
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
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@Valid @RequestBody JobRequest request) {
        try {
            Long recruiterId = getCurrentUserId();
            JobResponse response = jobService.createJob(recruiterId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/jobs/{jobId}")
    @Operation(
        summary = "Update a job posting",
        description = "Updates an existing job posting. Only the recruiter who created the job can update it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Job updated successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
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
            description = "Forbidden - user doesn't have permission to update this job"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Job not found"
        )
    })
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @Parameter(description = "Job ID", example = "1", required = true) @PathVariable Long jobId,
            @Valid @RequestBody JobRequest request) {
        try {
            Long recruiterId = getCurrentUserId();
            JobResponse response = jobService.updateJob(recruiterId, jobId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/jobs/{jobId}")
    @Operation(
        summary = "Delete a job posting",
        description = "Deletes a job posting. Only the recruiter who created the job can delete it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Job deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to delete this job"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Job not found"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @Parameter(description = "Job ID", example = "1", required = true) @PathVariable Long jobId) {
        try {
            Long recruiterId = getCurrentUserId();
            jobService.deleteJob(recruiterId, jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/jobs")
    @Operation(
        summary = "Get recruiter's jobs",
        description = "Retrieves a paginated list of all jobs posted by the authenticated recruiter. Supports pagination with page and size parameters."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Jobs retrieved successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getJobs(
            @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "20") @RequestParam(defaultValue = "20") int size) {
        try {
            Long recruiterId = getCurrentUserId();
            Pageable pageable = PageRequest.of(page, size);
            Page<JobResponse> jobs = jobService.getRecruiterJobs(recruiterId, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Jobs retrieved successfully", jobs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/jobs/status/{status}")
    @Operation(
        summary = "Get jobs by status",
        description = "Retrieves a paginated list of jobs filtered by status (active, closed, draft). Supports pagination."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Jobs retrieved successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getJobsByStatus(
            @Parameter(description = "Job status", example = "active", required = true) @PathVariable String status,
            @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "20") @RequestParam(defaultValue = "20") int size) {
        try {
            Long recruiterId = getCurrentUserId();
            Pageable pageable = PageRequest.of(page, size);
            Page<JobResponse> jobs = jobService.getRecruiterJobsByStatus(recruiterId, status, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Jobs retrieved successfully", jobs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/jobs/{jobId}")
    @Operation(
        summary = "Get job by ID",
        description = "Retrieves detailed information about a specific job by ID. Only accessible by the recruiter who created it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Job retrieved successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to view this job"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Job not found"
        )
    })
    public ResponseEntity<ApiResponse<JobResponse>> getJob(
            @Parameter(description = "Job ID", example = "1", required = true) @PathVariable Long jobId) {
        try {
            Long recruiterId = getCurrentUserId();
            JobResponse job = jobService.getJobById(recruiterId, jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job retrieved successfully", job));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Application Management Endpoints
    @GetMapping("/applications")
    @Operation(
        summary = "Get all applications",
        description = "Retrieves a paginated list of all applications for jobs posted by the authenticated recruiter."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Applications retrieved successfully",
            content = @Content(schema = @Schema(implementation = RecruiterApplicationResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Page<RecruiterApplicationResponse>>> getApplications(
            @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "20") @RequestParam(defaultValue = "20") int size) {
        try {
            Long recruiterId = getCurrentUserId();
            Pageable pageable = PageRequest.of(page, size);
            Page<RecruiterApplicationResponse> applications = applicationService.getApplications(recruiterId, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Applications retrieved successfully", applications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/applications/status/{status}")
    @Operation(
        summary = "Get applications by status",
        description = "Retrieves all applications filtered by status (pending, reviewing, shortlisted, rejected, hired)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Applications retrieved successfully",
            content = @Content(schema = @Schema(implementation = RecruiterApplicationResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<RecruiterApplicationResponse>>> getApplicationsByStatus(
            @Parameter(description = "Application status", example = "pending", required = true) @PathVariable String status) {
        try {
            Long recruiterId = getCurrentUserId();
            List<RecruiterApplicationResponse> applications = applicationService.getApplicationsByStatus(recruiterId, status);
            return ResponseEntity.ok(new ApiResponse<>(true, "Applications retrieved successfully", applications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/applications/{applicationId}")
    @Operation(
        summary = "Get application by ID",
        description = "Retrieves detailed information about a specific application including candidate details."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Application retrieved successfully",
            content = @Content(schema = @Schema(implementation = RecruiterApplicationResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to view this application"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Application not found"
        )
    })
    public ResponseEntity<ApiResponse<RecruiterApplicationResponse>> getApplication(
            @Parameter(description = "Application ID", example = "1", required = true) @PathVariable Long applicationId) {
        try {
            Long recruiterId = getCurrentUserId();
            RecruiterApplicationResponse application = applicationService.getApplicationById(recruiterId, applicationId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Application retrieved successfully", application));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/applications/{applicationId}/status")
    @Operation(
        summary = "Update application status",
        description = "Updates the status of an application (pending, reviewing, shortlisted, rejected, hired)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Application status updated successfully",
            content = @Content(schema = @Schema(implementation = RecruiterApplicationResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - invalid status or validation failed"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to update this application"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Application not found"
        )
    })
    public ResponseEntity<ApiResponse<RecruiterApplicationResponse>> updateApplicationStatus(
            @Parameter(description = "Application ID", example = "1", required = true) @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        try {
            Long recruiterId = getCurrentUserId();
            RecruiterApplicationResponse response = applicationService.updateApplicationStatus(recruiterId, applicationId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Application status updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Candidate Endpoints
    @GetMapping("/candidates")
    @Operation(
        summary = "Get all candidates",
        description = "Retrieves a list of all candidates who have applied to jobs posted by the authenticated recruiter."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Candidates retrieved successfully",
            content = @Content(schema = @Schema(implementation = CandidateResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<CandidateResponse>>> getCandidates() {
        try {
            Long recruiterId = getCurrentUserId();
            List<CandidateResponse> candidates = candidateService.getCandidates(recruiterId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Candidates retrieved successfully", candidates));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/candidates/{candidateId}")
    @Operation(
        summary = "Get candidate by ID",
        description = "Retrieves detailed information about a specific candidate including their profile and application statistics."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Candidate retrieved successfully",
            content = @Content(schema = @Schema(implementation = CandidateResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Candidate not found"
        )
    })
    public ResponseEntity<ApiResponse<CandidateResponse>> getCandidate(
            @Parameter(description = "Candidate ID", example = "1", required = true) @PathVariable Long candidateId) {
        try {
            Long recruiterId = getCurrentUserId();
            CandidateResponse candidate = candidateService.getCandidateById(recruiterId, candidateId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Candidate retrieved successfully", candidate));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Interview Endpoints
    @PostMapping("/interviews")
    @Operation(
        summary = "Schedule an interview",
        description = "Schedules a new interview for an application. Supports in-person, video, and phone interview types."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Interview scheduled successfully",
            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
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
            description = "Application not found"
        )
    })
    public ResponseEntity<ApiResponse<InterviewResponse>> scheduleInterview(@Valid @RequestBody InterviewRequest request) {
        try {
            Long recruiterId = getCurrentUserId();
            InterviewResponse response = interviewService.scheduleInterview(recruiterId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Interview scheduled successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/interviews/{interviewId}")
    @Operation(
        summary = "Update an interview",
        description = "Updates an existing interview. Only the recruiter who scheduled it can update it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Interview updated successfully",
            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
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
            description = "Forbidden - user doesn't have permission to update this interview"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Interview not found"
        )
    })
    public ResponseEntity<ApiResponse<InterviewResponse>> updateInterview(
            @Parameter(description = "Interview ID", example = "1", required = true) @PathVariable Long interviewId,
            @Valid @RequestBody InterviewRequest request) {
        try {
            Long recruiterId = getCurrentUserId();
            InterviewResponse response = interviewService.updateInterview(recruiterId, interviewId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Interview updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/interviews/{interviewId}")
    @Operation(
        summary = "Delete an interview",
        description = "Deletes an interview. Only the recruiter who scheduled it can delete it."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Interview deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - user doesn't have permission to delete this interview"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Interview not found"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteInterview(
            @Parameter(description = "Interview ID", example = "1", required = true) @PathVariable Long interviewId) {
        try {
            Long recruiterId = getCurrentUserId();
            interviewService.deleteInterview(recruiterId, interviewId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Interview deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/interviews")
    @Operation(
        summary = "Get all interviews",
        description = "Retrieves all interviews scheduled by the authenticated recruiter."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Interviews retrieved successfully",
            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getInterviews() {
        try {
            Long recruiterId = getCurrentUserId();
            List<InterviewResponse> interviews = interviewService.getInterviews(recruiterId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Interviews retrieved successfully", interviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/interviews/status/{status}")
    @Operation(
        summary = "Get interviews by status",
        description = "Retrieves interviews filtered by status (scheduled, completed, cancelled, rescheduled)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Interviews retrieved successfully",
            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getInterviewsByStatus(
            @Parameter(description = "Interview status", example = "scheduled", required = true) @PathVariable String status) {
        try {
            Long recruiterId = getCurrentUserId();
            List<InterviewResponse> interviews = interviewService.getInterviewsByStatus(recruiterId, status);
            return ResponseEntity.ok(new ApiResponse<>(true, "Interviews retrieved successfully", interviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/interviews/upcoming")
    @Operation(
        summary = "Get upcoming interviews",
        description = "Retrieves all upcoming interviews scheduled by the authenticated recruiter."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Upcoming interviews retrieved successfully",
            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getUpcomingInterviews() {
        try {
            Long recruiterId = getCurrentUserId();
            List<InterviewResponse> interviews = interviewService.getUpcomingInterviews(recruiterId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Upcoming interviews retrieved successfully", interviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/interviews/{interviewId}/status")
    @Operation(
        summary = "Update interview status",
        description = "Updates the status of an interview and optionally adds feedback."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Interview status updated successfully",
            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
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
            description = "Forbidden - user doesn't have permission to update this interview"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Interview not found"
        )
    })
    public ResponseEntity<ApiResponse<InterviewResponse>> updateInterviewStatus(
            @Parameter(description = "Interview ID", example = "1", required = true) @PathVariable Long interviewId,
            @Parameter(description = "New status", example = "completed", required = true) @RequestParam String status,
            @Parameter(description = "Optional feedback") @RequestParam(required = false) String feedback) {
        try {
            Long recruiterId = getCurrentUserId();
            InterviewResponse response = interviewService.updateInterviewStatus(recruiterId, interviewId, status, feedback);
            return ResponseEntity.ok(new ApiResponse<>(true, "Interview status updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
