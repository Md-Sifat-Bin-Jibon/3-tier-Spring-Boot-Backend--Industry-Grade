package com.example.demo.service;

import com.example.demo.dto.JobResponse;
import com.example.demo.entity.CandidateProfile;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.CandidateProfileRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CandidateProfileRepository profileRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository, CandidateProfileRepository profileRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public Page<JobResponse> getActiveJobs(Pageable pageable) {
        return jobRepository.findByStatus("active", pageable)
                .map(this::mapToResponse);
    }

    public Page<JobResponse> searchJobs(String query, Pageable pageable) {
        return jobRepository.searchActiveJobs(query, pageable)
                .map(this::mapToResponse);
    }

    public Page<JobResponse> getJobsWithFilters(String type, String location, String experienceLevel, String careerTrack, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByStatus("active", pageable);

        // Apply filters
        List<Job> filtered = jobs.getContent();
        if (type != null && !type.equals("all")) {
            filtered = filtered.stream().filter(j -> j.getType().equals(type)).collect(Collectors.toList());
        }
        if (location != null && !location.equals("all")) {
            if (location.equals("remote")) {
                filtered = filtered.stream().filter(j -> j.getLocation().toLowerCase().contains("remote")).collect(Collectors.toList());
            } else {
                filtered = filtered.stream().filter(j -> j.getLocation().equals(location)).collect(Collectors.toList());
            }
        }
        if (experienceLevel != null && !experienceLevel.equals("all")) {
            filtered = filtered.stream().filter(j -> j.getExperienceLevel().equals(experienceLevel)).collect(Collectors.toList());
        }
        if (careerTrack != null && !careerTrack.equals("all")) {
            filtered = filtered.stream().filter(j -> careerTrack.equals(j.getCareerTrack())).collect(Collectors.toList());
        }

        return jobRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public List<JobResponse> getMatchedJobs(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateProfile profile = profileRepository.findByUser(user).orElse(null);
        if (profile == null) {
            return jobRepository.findByStatus("active", Pageable.unpaged())
                    .map(this::mapToResponse)
                    .getContent();
        }

        List<Job> allJobs = jobRepository.findByStatus("active", Pageable.unpaged()).getContent();
        List<String> userSkills = profile.getSkills().stream()
                .map(s -> s.getName().toLowerCase())
                .collect(Collectors.toList());
        String preferredTrack = profile.getPreferredCareerTrack() != null 
                ? profile.getPreferredCareerTrack().toLowerCase() : "";
        String experienceLevel = profile.getExperienceLevel() != null 
                ? profile.getExperienceLevel().toLowerCase() : "";

        return allJobs.stream()
                .map(job -> {
                    JobResponse response = mapToResponse(job);
                    calculateMatchScore(response, job, userSkills, preferredTrack, experienceLevel);
                    return response;
                })
                .sorted((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()))
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        // Increment views
        job.setViews(job.getViews() + 1);
        jobRepository.save(job);
        
        return mapToResponse(job);
    }

    private void calculateMatchScore(JobResponse response, Job job, List<String> userSkills, String preferredTrack, String experienceLevel) {
        int matchScore = 0;
        List<String> matchReasons = new ArrayList<>();
        List<String> matchedSkills = new ArrayList<>();

        // Career track matching (30 points)
        if (!preferredTrack.isEmpty() && job.getCareerTrack() != null 
                && job.getCareerTrack().toLowerCase().equals(preferredTrack)) {
            matchScore += 30;
            matchReasons.add("Matches your preferred career track: " + job.getCareerTrack());
        }

        // Skill matching (50 points - up to 10 points per skill)
        List<String> jobSkills = job.getRequiredSkills().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        
        List<String> skillMatches = userSkills.stream()
                .filter(us -> jobSkills.stream().anyMatch(js -> js.contains(us) || us.contains(js)))
                .collect(Collectors.toList());

        if (!skillMatches.isEmpty()) {
            int skillPoints = Math.min(skillMatches.size() * 10, 50);
            matchScore += skillPoints;
            matchedSkills.addAll(skillMatches);
            matchReasons.add("Matches " + skillMatches.size() + " skill(s): " + 
                    String.join(", ", skillMatches.subList(0, Math.min(3, skillMatches.size()))) + 
                    (skillMatches.size() > 3 ? "..." : ""));
        }

        // Experience level matching (20 points)
        if (!experienceLevel.isEmpty()) {
            String jobExp = job.getExperienceLevel().toLowerCase();
            if (jobExp.equals("entry-level") && (experienceLevel.contains("entry") || experienceLevel.contains("student"))) {
                matchScore += 20;
                matchReasons.add("Perfect for entry-level candidates");
            } else if (jobExp.equals("junior") && (experienceLevel.contains("junior") || experienceLevel.contains("1-3"))) {
                matchScore += 20;
                matchReasons.add("Suitable for junior level");
            } else if (jobExp.equals("entry-level") || jobExp.equals("junior")) {
                matchScore += 10;
                matchReasons.add("Entry/junior level position");
            }
        }

        response.setMatchScore(matchScore);
        response.setMatchReasons(matchReasons);
        response.setMatchedSkills(matchedSkills);
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
