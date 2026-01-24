package com.example.demo.service;

import com.example.demo.dto.ApplicationResponse;
import com.example.demo.entity.Job;
import com.example.demo.entity.JobApplication;
import com.example.demo.entity.User;
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
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FruvoCoinService fruvoCoinService;

    public ApplicationService(JobApplicationRepository applicationRepository, JobRepository jobRepository, 
                              UserRepository userRepository, FruvoCoinService fruvoCoinService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.fruvoCoinService = fruvoCoinService;
    }

    public ApplicationResponse applyToJob(Long userId, Long jobId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if already applied
        if (applicationRepository.existsByCandidateAndJob(candidate, job)) {
            throw new RuntimeException("You have already applied for this job");
        }

        // Check and deduct coins if required
        Integer coinsDeducted = null;
        if (job.getFruvoCoinRequired() != null && job.getFruvoCoinRequired() > 0) {
            if (!fruvoCoinService.hasEnoughCoins(userId, job.getFruvoCoinRequired())) {
                throw new RuntimeException("Insufficient Fruvo coins. Required: " + job.getFruvoCoinRequired());
            }
            fruvoCoinService.deductCoins(userId, job.getFruvoCoinRequired());
            coinsDeducted = job.getFruvoCoinRequired();
        }

        JobApplication application = new JobApplication(candidate, job);
        application.setCoinsDeducted(coinsDeducted);
        application = applicationRepository.save(application);

        return mapToResponse(application);
    }

    public List<ApplicationResponse> getApplications(Long userId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository.findByCandidate(candidate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<ApplicationResponse> getApplications(Long userId, Pageable pageable) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository.findByCandidate(candidate, pageable)
                .map(this::mapToResponse);
    }

    public List<ApplicationResponse> getApplicationsByStatus(Long userId, String status) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository.findByCandidateAndStatus(candidate, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public long getApplicationCount(Long userId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.countByCandidate(candidate);
    }

    public long getApplicationCountByStatus(Long userId, String status) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.countByCandidateAndStatus(candidate, status);
    }

    public boolean hasApplied(Long userId, Long jobId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return applicationRepository.existsByCandidateAndJob(candidate, job);
    }

    private ApplicationResponse mapToResponse(JobApplication application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompany(application.getJob().getCompany());
        response.setLocation(application.getJob().getLocation());
        response.setJobType(application.getJob().getType());
        response.setStatus(application.getStatus());
        response.setAppliedDate(application.getAppliedDate());
        response.setSalary(application.getJob().getSalary());
        response.setCareerTrack(application.getJob().getCareerTrack());
        return response;
    }
}
