package com.example.demo.service;

import com.example.demo.dto.CandidateProfileRequest;
import com.example.demo.dto.CandidateProfileResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateProfileService {

    private final CandidateProfileRepository profileRepository;
    private final UserRepository userRepository;

    public CandidateProfileService(CandidateProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public CandidateProfileResponse createOrUpdateProfile(Long userId, CandidateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateProfile profile = profileRepository.findByUser(user)
                .orElse(new CandidateProfile(user));

        // Update basic fields
        profile.setFullName(request.getFullName());
        profile.setEducationLevel(request.getEducationLevel());
        profile.setExperienceLevel(request.getExperienceLevel());
        profile.setPreferredCareerTrack(request.getPreferredCareerTrack());
        profile.setTargetRole(request.getTargetRole());

        // Update skills
        if (request.getSkills() != null) {
            profile.getSkills().clear();
            final CandidateProfile finalProfile = profile;
            request.getSkills().forEach(skillReq -> {
                Skill skill = new Skill(skillReq.getName());
                skill.setProfile(finalProfile);
                finalProfile.getSkills().add(skill);
            });
        }

        // Update projects
        if (request.getProjects() != null) {
            profile.getProjects().clear();
            final CandidateProfile finalProfile = profile;
            request.getProjects().forEach(projectReq -> {
                Project project = new Project(
                        projectReq.getTitle(),
                        projectReq.getDescription(),
                        projectReq.getTechnologies(),
                        projectReq.getLink()
                );
                project.setProfile(finalProfile);
                finalProfile.getProjects().add(project);
            });
        }

        // Update experiences
        if (request.getExperiences() != null) {
            profile.getExperiences().clear();
            final CandidateProfile finalProfile = profile;
            request.getExperiences().forEach(expReq -> {
                WorkExperience exp = new WorkExperience(
                        expReq.getCompany(),
                        expReq.getPosition(),
                        LocalDate.parse(expReq.getStartDate()),
                        expReq.getEndDate() != null && !expReq.getEndDate().isEmpty() 
                                ? LocalDate.parse(expReq.getEndDate()) : null,
                        expReq.getCurrent() != null ? expReq.getCurrent() : false,
                        expReq.getDescription()
                );
                exp.setProfile(finalProfile);
                finalProfile.getExperiences().add(exp);
            });
        }

        // Update educations
        if (request.getEducations() != null) {
            profile.getEducations().clear();
            final CandidateProfile finalProfile = profile;
            request.getEducations().forEach(eduReq -> {
                Education edu = new Education(
                        eduReq.getInstitution(),
                        eduReq.getDegree(),
                        eduReq.getField(),
                        LocalDate.parse(eduReq.getStartDate()),
                        eduReq.getEndDate() != null && !eduReq.getEndDate().isEmpty() 
                                ? LocalDate.parse(eduReq.getEndDate()) : null,
                        eduReq.getCurrent() != null ? eduReq.getCurrent() : false,
                        eduReq.getGpa()
                );
                edu.setProfile(finalProfile);
                finalProfile.getEducations().add(edu);
            });
        }

        profile = profileRepository.save(profile);
        return mapToResponse(profile);
    }

    public CandidateProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(profile);
    }

    public boolean profileExists(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return profileRepository.existsByUser(user);
    }

    private CandidateProfileResponse mapToResponse(CandidateProfile profile) {
        CandidateProfileResponse response = new CandidateProfileResponse();
        response.setId(profile.getId());
        response.setFullName(profile.getFullName());
        response.setEducationLevel(profile.getEducationLevel());
        response.setExperienceLevel(profile.getExperienceLevel());
        response.setPreferredCareerTrack(profile.getPreferredCareerTrack());
        response.setTargetRole(profile.getTargetRole());
        response.setCvFileName(profile.getCvFileName());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());

        // Map skills
        response.setSkills(profile.getSkills().stream()
                .map(Skill::getName)
                .collect(Collectors.toList()));

        // Map projects
        response.setProjects(profile.getProjects().stream()
                .map(p -> {
                    CandidateProfileResponse.ProjectResponse pr = new CandidateProfileResponse.ProjectResponse();
                    pr.setId(p.getId());
                    pr.setTitle(p.getTitle());
                    pr.setDescription(p.getDescription());
                    pr.setTechnologies(p.getTechnologies());
                    pr.setLink(p.getLink());
                    return pr;
                })
                .collect(Collectors.toList()));

        // Map experiences
        response.setExperiences(profile.getExperiences().stream()
                .map(e -> {
                    CandidateProfileResponse.WorkExperienceResponse er = new CandidateProfileResponse.WorkExperienceResponse();
                    er.setId(e.getId());
                    er.setCompany(e.getCompany());
                    er.setPosition(e.getPosition());
                    er.setStartDate(e.getStartDate().toString());
                    er.setEndDate(e.getEndDate() != null ? e.getEndDate().toString() : null);
                    er.setCurrent(e.getCurrent());
                    er.setDescription(e.getDescription());
                    return er;
                })
                .collect(Collectors.toList()));

        // Map educations
        response.setEducations(profile.getEducations().stream()
                .map(e -> {
                    CandidateProfileResponse.EducationResponse er = new CandidateProfileResponse.EducationResponse();
                    er.setId(e.getId());
                    er.setInstitution(e.getInstitution());
                    er.setDegree(e.getDegree());
                    er.setField(e.getField());
                    er.setStartDate(e.getStartDate().toString());
                    er.setEndDate(e.getEndDate() != null ? e.getEndDate().toString() : null);
                    er.setCurrent(e.getCurrent());
                    er.setGpa(e.getGpa());
                    return er;
                })
                .collect(Collectors.toList()));

        return response;
    }
}
