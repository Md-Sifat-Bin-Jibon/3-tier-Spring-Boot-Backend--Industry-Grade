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
@RequestMapping("/api/candidate")
@Tag(name = "Candidate Management", description = "API endpoints for candidate operations")
public class CandidateController {

    private final CandidateProfileService profileService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;
    private final FruvoCoinService fruvoCoinService;
    private final com.example.demo.repository.UserRepository userRepository;

    public CandidateController(CandidateProfileService profileService, JobService jobService,
                               ApplicationService applicationService, SavedJobService savedJobService,
                               FruvoCoinService fruvoCoinService,
                               com.example.demo.repository.UserRepository userRepository) {
        this.profileService = profileService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.savedJobService = savedJobService;
        this.fruvoCoinService = fruvoCoinService;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    // Profile Endpoints
    @PostMapping("/profile")
    @Operation(
        summary = "Create or update candidate profile",
        description = "Creates a new profile or updates existing one. All profile data including skills, projects, experiences, and education will be saved."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Profile saved successfully",
            content = @Content(schema = @Schema(implementation = CandidateProfileResponse.class))
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
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> createOrUpdateProfile(
            @Valid @RequestBody CandidateProfileRequest request) {
        try {
            Long userId = getCurrentUserId();
            CandidateProfileResponse response = profileService.createOrUpdateProfile(userId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Profile saved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/profile")
    @Operation(
        summary = "Get candidate profile",
        description = "Retrieves the current candidate's complete profile including skills, projects, experiences, and education"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = CandidateProfileResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Profile not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getProfile() {
        try {
            Long userId = getCurrentUserId();
            CandidateProfileResponse response = profileService.getProfile(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Job Endpoints
    @GetMapping("/jobs")
    @Operation(
        summary = "Get active jobs",
        description = "Retrieves a paginated list of all active job postings. Supports pagination with page and size parameters."
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
            Pageable pageable = PageRequest.of(page, size);
            Page<JobResponse> jobs = jobService.getActiveJobs(pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Jobs retrieved successfully", jobs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/jobs/search")
    @Operation(
        summary = "Search jobs",
        description = "Searches active jobs by query string. Searches in job title, company name, description, and required skills."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - invalid query parameter"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Page<JobResponse>>> searchJobs(
            @Parameter(description = "Search query string", example = "software engineer", required = true) @RequestParam String query,
            @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "20") @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<JobResponse> jobs = jobService.searchJobs(query, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, "Search completed successfully", jobs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/jobs/matched")
    @Operation(
        summary = "Get matched jobs",
        description = "Retrieves jobs matched to candidate's profile based on skills, career track, and experience level. Jobs are sorted by match score."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Matched jobs retrieved successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<JobResponse>>> getMatchedJobs() {
        try {
            Long userId = getCurrentUserId();
            List<JobResponse> jobs = jobService.getMatchedJobs(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Matched jobs retrieved successfully", jobs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/jobs/{jobId}")
    @Operation(
        summary = "Get job by ID",
        description = "Retrieves detailed information about a specific job by ID. Also increments the view count."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Job retrieved successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Job not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<JobResponse>> getJob(
            @Parameter(description = "Job ID", example = "1", required = true) @PathVariable Long jobId) {
        try {
            JobResponse job = jobService.getJobById(jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job retrieved successfully", job));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Application Endpoints
    @PostMapping("/applications/{jobId}")
    @Operation(
        summary = "Apply to a job",
        description = "Submits an application for a job. If the job requires Fruvo coins, they will be deducted automatically."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Application submitted successfully",
            content = @Content(schema = @Schema(implementation = ApplicationResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - already applied or insufficient coins"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Job not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyToJob(
            @Parameter(description = "Job ID to apply for", example = "1", required = true) @PathVariable Long jobId) {
        try {
            Long userId = getCurrentUserId();
            ApplicationResponse response = applicationService.applyToJob(userId, jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Application submitted successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/applications")
    @Operation(
        summary = "Get applications",
        description = "Retrieves all applications for the candidate. Optionally filter by status (pending, reviewing, shortlisted, rejected, hired)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Applications retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApplicationResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getApplications(
            @Parameter(description = "Filter by application status (pending, reviewing, shortlisted, rejected, hired)", example = "pending") @RequestParam(required = false) String status) {
        try {
            Long userId = getCurrentUserId();
            List<ApplicationResponse> applications;
            if (status != null && !status.equals("all")) {
                applications = applicationService.getApplicationsByStatus(userId, status);
            } else {
                applications = applicationService.getApplications(userId);
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Applications retrieved successfully", applications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/applications/{jobId}/check")
    @Operation(
        summary = "Check if applied",
        description = "Checks if the candidate has already applied to a specific job"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Check completed successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Boolean>> hasApplied(
            @Parameter(description = "Job ID to check", example = "1", required = true) @PathVariable Long jobId) {
        try {
            Long userId = getCurrentUserId();
            boolean applied = applicationService.hasApplied(userId, jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Check completed", applied));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Saved Jobs Endpoints
    @PostMapping("/saved-jobs/{jobId}")
    @Operation(
        summary = "Save a job",
        description = "Saves a job to the candidate's saved jobs list for later viewing"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Job saved successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - job already saved or job not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Void>> saveJob(
            @Parameter(description = "Job ID to save", example = "1", required = true) @PathVariable Long jobId) {
        try {
            Long userId = getCurrentUserId();
            savedJobService.saveJob(userId, jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job saved successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/saved-jobs/{jobId}")
    @Operation(
        summary = "Unsave a job",
        description = "Removes a job from the candidate's saved jobs list"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Job removed from saved jobs successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Void>> unsaveJob(
            @Parameter(description = "Job ID to unsave", example = "1", required = true) @PathVariable Long jobId) {
        try {
            Long userId = getCurrentUserId();
            savedJobService.unsaveJob(userId, jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job removed from saved jobs", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/saved-jobs")
    @Operation(
        summary = "Get saved jobs",
        description = "Retrieves all jobs saved by the candidate"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Saved jobs retrieved successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<List<JobResponse>>> getSavedJobs() {
        try {
            Long userId = getCurrentUserId();
            List<JobResponse> jobs = savedJobService.getSavedJobs(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Saved jobs retrieved successfully", jobs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/saved-jobs/{jobId}/check")
    @Operation(
        summary = "Check if job is saved",
        description = "Checks if a specific job is in the candidate's saved jobs list"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Check completed successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Boolean>> isJobSaved(
            @Parameter(description = "Job ID to check", example = "1", required = true) @PathVariable Long jobId) {
        try {
            Long userId = getCurrentUserId();
            boolean saved = savedJobService.isJobSaved(userId, jobId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Check completed", saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Fruvo Coin Endpoints
    @GetMapping("/coins")
    @Operation(
        summary = "Get Fruvo coins",
        description = "Retrieves the candidate's current Fruvo coins balance and score"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Coins retrieved successfully",
            content = @Content(schema = @Schema(implementation = FruvoCoinResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<FruvoCoinResponse>> getCoins() {
        try {
            Long userId = getCurrentUserId();
            FruvoCoinResponse response = fruvoCoinService.getCoins(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Coins retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/coins/check/{amount}")
    @Operation(
        summary = "Check if has enough coins",
        description = "Checks if the candidate has enough Fruvo coins to cover the specified amount"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Check completed successfully",
            content = @Content(schema = @Schema(implementation = JobResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<Boolean>> hasEnoughCoins(
            @Parameter(description = "Amount of coins to check", example = "50", required = true) @PathVariable Integer amount) {
        try {
            Long userId = getCurrentUserId();
            boolean hasEnough = fruvoCoinService.hasEnoughCoins(userId, amount);
            return ResponseEntity.ok(new ApiResponse<>(true, "Check completed", hasEnough));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Dashboard Endpoints
    @GetMapping("/dashboard/stats")
    @Operation(
        summary = "Get dashboard statistics",
        description = "Retrieves comprehensive dashboard statistics including application counts, saved jobs, and Fruvo coins"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Statistics retrieved successfully",
            content = @Content(schema = @Schema(implementation = DashboardStatsResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - user not authenticated"
        )
    })
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        try {
            Long userId = getCurrentUserId();
            DashboardStatsResponse stats = new DashboardStatsResponse();
            
            stats.setTotalApplications(applicationService.getApplicationCount(userId));
            stats.setPendingApplications(applicationService.getApplicationCountByStatus(userId, "pending") + 
                                       applicationService.getApplicationCountByStatus(userId, "reviewing"));
            stats.setShortlisted(applicationService.getApplicationCountByStatus(userId, "shortlisted"));
            stats.setRejected(applicationService.getApplicationCountByStatus(userId, "rejected"));
            stats.setHired(applicationService.getApplicationCountByStatus(userId, "hired"));
            stats.setUpcomingInterviews(0L); // TODO: Implement interview count
            stats.setSavedJobs(savedJobService.getSavedJobCount(userId));
            
            FruvoCoinResponse coins = fruvoCoinService.getCoins(userId);
            stats.setCoins(coins.getCoins());
            stats.setScore(coins.getScore());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Statistics retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
