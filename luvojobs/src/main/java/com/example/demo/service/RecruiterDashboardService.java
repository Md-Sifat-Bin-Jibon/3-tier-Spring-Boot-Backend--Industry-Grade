package com.example.demo.service;

import com.example.demo.dto.RecruiterDashboardStatsResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class RecruiterDashboardService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public RecruiterDashboardService(UserRepository userRepository,
                                     JobRepository jobRepository,
                                     JobApplicationRepository applicationRepository,
                                     InterviewRepository interviewRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    public RecruiterDashboardStatsResponse getDashboardStats(Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        RecruiterDashboardStatsResponse stats = new RecruiterDashboardStatsResponse();

        stats.setTotalJobs(jobRepository.countByRecruiter(recruiter));
        stats.setActiveJobs(jobRepository.countByRecruiterAndStatus(recruiter, "active"));
        stats.setTotalApplications(applicationRepository.countByJob_Recruiter(recruiter));
        stats.setPendingApplications(
                applicationRepository.countByJob_RecruiterAndStatus(recruiter, "pending") +
                applicationRepository.countByJob_RecruiterAndStatus(recruiter, "reviewing")
        );
        stats.setShortlisted(applicationRepository.countByJob_RecruiterAndStatus(recruiter, "shortlisted"));
        stats.setHired(applicationRepository.countByJob_RecruiterAndStatus(recruiter, "hired"));
        stats.setRejected(applicationRepository.countByJob_RecruiterAndStatus(recruiter, "rejected"));

        // Get unique candidates count
        long uniqueCandidates = applicationRepository.findByJob_Recruiter(recruiter).stream()
                .map(app -> app.getCandidate().getId())
                .distinct()
                .count();
        stats.setTotalCandidates(uniqueCandidates);

        // Get upcoming interviews
        long upcomingInterviews = interviewRepository.findByRecruiterAndScheduledDateAfter(recruiter, LocalDateTime.now())
                .stream()
                .filter(interview -> "scheduled".equals(interview.getStatus()))
                .count();
        stats.setUpcomingInterviews(upcomingInterviews);

        return stats;
    }
}
