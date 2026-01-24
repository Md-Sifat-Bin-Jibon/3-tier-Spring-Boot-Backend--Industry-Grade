package com.example.demo.repository;

import com.example.demo.entity.Interview;
import com.example.demo.entity.JobApplication;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByRecruiter(User recruiter);
    List<Interview> findByApplication(JobApplication application);
    List<Interview> findByRecruiterAndStatus(User recruiter, String status);
    List<Interview> findByRecruiterAndScheduledDateAfter(User recruiter, LocalDateTime date);
    Optional<Interview> findByApplicationAndRecruiter(JobApplication application, User recruiter);
}
