package com.example.demo.service;

import com.example.demo.dto.ApplicationStatusUpdateRequest;
import com.example.demo.dto.RecruiterApplicationResponse;
import com.example.demo.entity.CandidateProfile;
import com.example.demo.entity.Job;
import com.example.demo.entity.JobApplication;
import com.example.demo.entity.User;
import com.example.demo.repository.CandidateProfileRepository;
import com.example.demo.repository.JobApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecruiterApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CandidateProfileRepository profileRepository;

    public RecruiterApplicationService(JobApplicationRepository applicationRepository,
                                      JobRepository jobRepository,
                                      UserRepository userRepository,
                                      CandidateProfileRepository profileRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public Page<RecruiterApplicationResponse> getApplications(Long recruiterId, Pageable pageable) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        List<JobApplication> applications = applicationRepository.findByJob_Recruiter(recruiter);
        
        // Convert to page manually (for simplicity, in production use proper pagination)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), applications.size());
        List<JobApplication> pagedApplications = applications.subList(start, end);
        
        return new org.springframework.data.domain.PageImpl<>(
                pagedApplications.stream()
                        .map(app -> mapToResponse(app))
                        .collect(Collectors.toList()),
                pageable,
                applications.size()
        );
    }

    public List<RecruiterApplicationResponse> getApplicationsByStatus(Long recruiterId, String status) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        List<JobApplication> applications = applicationRepository.findByJob_RecruiterAndStatus(recruiter, status);
        return applications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RecruiterApplicationResponse getApplicationById(Long recruiterId, Long applicationId) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to view this application");
        }

        return mapToResponse(application);
    }

    public RecruiterApplicationResponse updateApplicationStatus(Long recruiterId, Long applicationId, 
                                                               ApplicationStatusUpdateRequest request) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to update this application");
        }

        application.setStatus(request.getStatus());
        application = applicationRepository.save(application);

        return mapToResponse(application);
    }

    public long getApplicationCount(Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return applicationRepository.countByJob_Recruiter(recruiter);
    }

    public long getApplicationCountByStatus(Long recruiterId, String status) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return applicationRepository.countByJob_RecruiterAndStatus(recruiter, status);
    }

    private RecruiterApplicationResponse mapToResponse(JobApplication application) {
        RecruiterApplicationResponse response = new RecruiterApplicationResponse();
        response.setId(application.getId());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompany(application.getJob().getCompany());
        response.setLocation(application.getJob().getLocation());
        response.setJobType(application.getJob().getType());
        response.setStatus(application.getStatus());
        response.setAppliedDate(application.getAppliedDate());

        // Get candidate details
        User candidate = application.getCandidate();
        response.setCandidateId(candidate.getId());
        response.setCandidateName(candidate.getUsername()); // Using username as name
        response.setCandidateEmail(candidate.getEmail());

        // Get candidate profile if exists
        CandidateProfile profile = profileRepository.findByUserId(candidate.getId()).orElse(null);
        if (profile != null) {
            response.setCandidatePhone(null); // Phone not available in profile
            response.setExperience(profile.getExperienceLevel());
            response.setEducation(profile.getEducationLevel());
            response.setSkills(profile.getSkills().stream()
                    .map(skill -> skill.getName())
                    .collect(Collectors.toList()));
        }

        return response;
    }
}
