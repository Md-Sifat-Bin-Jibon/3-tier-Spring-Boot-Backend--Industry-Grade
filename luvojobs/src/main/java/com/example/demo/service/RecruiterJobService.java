package com.example.demo.service;

import com.example.demo.dto.JobRequest;
import com.example.demo.dto.JobResponse;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.JobApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecruiterJobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobApplicationRepository applicationRepository;

    public RecruiterJobService(JobRepository jobRepository, UserRepository userRepository,
                               JobApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public JobResponse createJob(Long recruiterId, JobRequest request) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setType(request.getType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements() != null ? request.getRequirements() : new ArrayList<>());
        job.setRequiredSkills(request.getRequiredSkills() != null ? request.getRequiredSkills() : new ArrayList<>());
        job.setSalary(request.getSalary());
        job.setCareerTrack(request.getCareerTrack());
        job.setDeadline(request.getDeadline());
        job.setStatus(request.getStatus() != null ? request.getStatus() : "draft");
        job.setFruvoCoinRequired(request.getFruvoCoinRequired());
        job.setPostedDate(LocalDate.now());
        job.setViews(0);

        job = jobRepository.save(job);
        return mapToResponse(job);
    }

    public JobResponse updateJob(Long recruiterId, Long jobId, JobRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to update this job");
        }

        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setType(request.getType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements() != null ? request.getRequirements() : new ArrayList<>());
        job.setRequiredSkills(request.getRequiredSkills() != null ? request.getRequiredSkills() : new ArrayList<>());
        job.setSalary(request.getSalary());
        job.setCareerTrack(request.getCareerTrack());
        job.setDeadline(request.getDeadline());
        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }
        job.setFruvoCoinRequired(request.getFruvoCoinRequired());

        job = jobRepository.save(job);
        return mapToResponse(job);
    }

    public void deleteJob(Long recruiterId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to delete this job");
        }

        jobRepository.delete(job);
    }

    public Page<JobResponse> getRecruiterJobs(Long recruiterId, Pageable pageable) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return jobRepository.findByRecruiter(recruiter, pageable)
                .map(this::mapToResponse);
    }

    public Page<JobResponse> getRecruiterJobsByStatus(Long recruiterId, String status, Pageable pageable) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return jobRepository.findByRecruiterAndStatus(recruiter, status, pageable)
                .map(this::mapToResponse);
    }

    public JobResponse getJobById(Long recruiterId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to view this job");
        }

        return mapToResponse(job);
    }

    private JobResponse mapToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setType(job.getType());
        response.setExperienceLevel(job.getExperienceLevel());
        response.setDescription(job.getDescription());
        response.setRequirements(job.getRequirements());
        response.setRequiredSkills(job.getRequiredSkills());
        response.setSalary(job.getSalary());
        response.setCareerTrack(job.getCareerTrack());
        response.setPostedDate(job.getPostedDate());
        response.setDeadline(job.getDeadline());
        response.setStatus(job.getStatus());
        response.setFruvoCoinRequired(job.getFruvoCoinRequired());
        response.setViews(job.getViews());
        
        // Get application count
        long applicationCount = applicationRepository.findByJob(job).size();
        // Note: We can't set applicationCount directly in JobResponse, but we can add it if needed
        
        return response;
    }
}
