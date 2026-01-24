package com.example.demo.repository;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = 'active' AND " +
           "(LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(j.company) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Job> searchActiveJobs(@Param("query") String query, Pageable pageable);
    
    List<Job> findByStatusAndType(String status, String type);
    List<Job> findByStatusAndLocationContainingIgnoreCase(String status, String location);
    List<Job> findByStatusAndExperienceLevel(String status, String experienceLevel);
    List<Job> findByStatusAndCareerTrack(String status, String careerTrack);
    
    // Recruiter-specific methods
    List<Job> findByRecruiter(User recruiter);
    Page<Job> findByRecruiter(User recruiter, Pageable pageable);
    List<Job> findByRecruiterAndStatus(User recruiter, String status);
    Page<Job> findByRecruiterAndStatus(User recruiter, String status, Pageable pageable);
    long countByRecruiter(User recruiter);
    long countByRecruiterAndStatus(User recruiter, String status);
}
