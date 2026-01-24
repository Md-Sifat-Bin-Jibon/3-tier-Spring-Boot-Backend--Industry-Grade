package com.example.demo.service;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.entity.CandidateProfile;
import com.example.demo.entity.JobApplication;
import com.example.demo.entity.User;
import com.example.demo.repository.CandidateProfileRepository;
import com.example.demo.repository.JobApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository profileRepository;
    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public CandidateService(UserRepository userRepository,
                           CandidateProfileRepository profileRepository,
                           JobApplicationRepository applicationRepository,
                           JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    public List<CandidateResponse> getCandidates(Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        // Get all candidates who applied to jobs posted by this recruiter
        List<JobApplication> applications = applicationRepository.findByJob_Recruiter(recruiter);
        
        return applications.stream()
                .map(JobApplication::getCandidate)
                .distinct()
                .map(candidate -> mapToResponse(candidate, recruiter))
                .collect(Collectors.toList());
    }

    public CandidateResponse getCandidateById(Long recruiterId, Long candidateId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // Verify candidate has applied to at least one job by this recruiter
        boolean hasApplied = applicationRepository.findByJob_Recruiter(recruiter).stream()
                .anyMatch(app -> app.getCandidate().getId().equals(candidateId));

        if (!hasApplied) {
            throw new RuntimeException("Candidate not found or has not applied to your jobs");
        }

        return mapToResponse(candidate, recruiter);
    }

    private CandidateResponse mapToResponse(User candidate, User recruiter) {
        CandidateResponse response = new CandidateResponse();
        response.setUserId(candidate.getId());
        response.setName(candidate.getUsername());
        response.setEmail(candidate.getEmail());

        CandidateProfile profile = profileRepository.findByUserId(candidate.getId()).orElse(null);
        if (profile != null) {
            response.setPhone(null); // Phone not available in profile
            response.setCareerTrack(profile.getPreferredCareerTrack());
            response.setExperienceLevel(profile.getExperienceLevel());
            response.setSkills(profile.getSkills().stream()
                    .map(skill -> skill.getName())
                    .collect(Collectors.toList()));
        }

        // Get application statistics for this recruiter's jobs
        List<JobApplication> applications = applicationRepository.findByCandidate(candidate).stream()
                .filter(app -> app.getJob().getRecruiter().getId().equals(recruiter.getId()))
                .collect(Collectors.toList());

        response.setTotalApplications((long) applications.size());
        response.setShortlistedApplications(applications.stream()
                .filter(app -> "shortlisted".equals(app.getStatus()))
                .count());
        response.setHiredApplications(applications.stream()
                .filter(app -> "hired".equals(app.getStatus()))
                .count());

        return response;
    }
}
