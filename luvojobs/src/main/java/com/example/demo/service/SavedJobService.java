package com.example.demo.service;

import com.example.demo.dto.JobResponse;
import com.example.demo.entity.Job;
import com.example.demo.entity.SavedJob;
import com.example.demo.entity.User;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.SavedJobRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public SavedJobService(SavedJobRepository savedJobRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public void saveJob(Long userId, Long jobId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (savedJobRepository.existsByCandidateAndJob(candidate, job)) {
            throw new RuntimeException("Job already saved");
        }

        SavedJob savedJob = new SavedJob(candidate, job);
        savedJobRepository.save(savedJob);
    }

    public void unsaveJob(Long userId, Long jobId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        savedJobRepository.deleteByCandidateAndJob(candidate, job);
    }

    public List<JobResponse> getSavedJobs(Long userId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedJobRepository.findByCandidate(candidate).stream()
                .map(SavedJob::getJob)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<JobResponse> getSavedJobs(Long userId, Pageable pageable) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedJobRepository.findByCandidate(candidate, pageable)
                .map(SavedJob::getJob)
                .map(this::mapToResponse);
    }

    public boolean isJobSaved(Long userId, Long jobId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return savedJobRepository.existsByCandidateAndJob(candidate, job);
    }

    public long getSavedJobCount(Long userId) {
        User candidate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return savedJobRepository.findByCandidate(candidate).size();
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
        return response;
    }
}
