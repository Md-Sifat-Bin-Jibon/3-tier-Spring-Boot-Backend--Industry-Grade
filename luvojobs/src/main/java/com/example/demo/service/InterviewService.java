package com.example.demo.service;

import com.example.demo.dto.InterviewRequest;
import com.example.demo.dto.InterviewResponse;
import com.example.demo.entity.Interview;
import com.example.demo.entity.JobApplication;
import com.example.demo.entity.User;
import com.example.demo.repository.InterviewRepository;
import com.example.demo.repository.JobApplicationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public InterviewService(InterviewRepository interviewRepository,
                            JobApplicationRepository applicationRepository,
                            UserRepository userRepository) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public InterviewResponse scheduleInterview(Long recruiterId, InterviewRequest request) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        JobApplication application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to schedule interview for this application");
        }

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setRecruiter(recruiter);
        interview.setScheduledDate(request.getScheduledDate());
        interview.setDurationMinutes(request.getDurationMinutes());
        interview.setType(request.getType());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setLocation(request.getLocation());
        interview.setNotes(request.getNotes());
        interview.setStatus("scheduled");

        interview = interviewRepository.save(interview);
        return mapToResponse(interview);
    }

    public InterviewResponse updateInterview(Long recruiterId, Long interviewId, InterviewRequest request) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        if (!interview.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to update this interview");
        }

        interview.setScheduledDate(request.getScheduledDate());
        interview.setDurationMinutes(request.getDurationMinutes());
        interview.setType(request.getType());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setLocation(request.getLocation());
        interview.setNotes(request.getNotes());

        interview = interviewRepository.save(interview);
        return mapToResponse(interview);
    }

    public void deleteInterview(Long recruiterId, Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        if (!interview.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to delete this interview");
        }

        interviewRepository.delete(interview);
    }

    public List<InterviewResponse> getInterviews(Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return interviewRepository.findByRecruiter(recruiter).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InterviewResponse> getInterviewsByStatus(Long recruiterId, String status) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return interviewRepository.findByRecruiterAndStatus(recruiter, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InterviewResponse> getUpcomingInterviews(Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return interviewRepository.findByRecruiterAndScheduledDateAfter(recruiter, LocalDateTime.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InterviewResponse updateInterviewStatus(Long recruiterId, Long interviewId, String status, String feedback) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        if (!interview.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You don't have permission to update this interview");
        }

        interview.setStatus(status);
        if (feedback != null) {
            interview.setFeedback(feedback);
        }

        interview = interviewRepository.save(interview);
        return mapToResponse(interview);
    }

    private InterviewResponse mapToResponse(Interview interview) {
        InterviewResponse response = new InterviewResponse();
        response.setId(interview.getId());
        response.setApplicationId(interview.getApplication().getId());
        response.setJobTitle(interview.getApplication().getJob().getTitle());
        response.setCandidateName(interview.getApplication().getCandidate().getUsername());
        response.setCandidateEmail(interview.getApplication().getCandidate().getEmail());
        response.setScheduledDate(interview.getScheduledDate());
        response.setDurationMinutes(interview.getDurationMinutes());
        response.setType(interview.getType());
        response.setStatus(interview.getStatus());
        response.setMeetingLink(interview.getMeetingLink());
        response.setLocation(interview.getLocation());
        response.setNotes(interview.getNotes());
        response.setFeedback(interview.getFeedback());
        response.setCreatedAt(interview.getCreatedAt());
        response.setUpdatedAt(interview.getUpdatedAt());
        return response;
    }
}
