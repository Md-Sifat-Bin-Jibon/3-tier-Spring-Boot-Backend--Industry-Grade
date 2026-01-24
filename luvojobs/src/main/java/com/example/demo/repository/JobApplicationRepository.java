package com.example.demo.repository;

import com.example.demo.entity.JobApplication;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Optional<JobApplication> findByCandidateAndJob(User candidate, Job job);
    boolean existsByCandidateAndJob(User candidate, Job job);
    List<JobApplication> findByCandidate(User candidate);
    Page<JobApplication> findByCandidate(User candidate, Pageable pageable);
    List<JobApplication> findByCandidateAndStatus(User candidate, String status);
    long countByCandidate(User candidate);
    long countByCandidateAndStatus(User candidate, String status);
    
    // Recruiter-specific methods
    List<JobApplication> findByJob(Job job);
    List<JobApplication> findByJobAndStatus(Job job, String status);
    List<JobApplication> findByJob_Recruiter(User recruiter);
    List<JobApplication> findByJob_RecruiterAndStatus(User recruiter, String status);
    long countByJob_Recruiter(User recruiter);
    long countByJob_RecruiterAndStatus(User recruiter, String status);
}
