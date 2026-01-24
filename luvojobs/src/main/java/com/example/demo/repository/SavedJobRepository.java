package com.example.demo.repository;

import com.example.demo.entity.SavedJob;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    Optional<SavedJob> findByCandidateAndJob(User candidate, Job job);
    boolean existsByCandidateAndJob(User candidate, Job job);
    List<SavedJob> findByCandidate(User candidate);
    Page<SavedJob> findByCandidate(User candidate, Pageable pageable);
    void deleteByCandidateAndJob(User candidate, Job job);
}
