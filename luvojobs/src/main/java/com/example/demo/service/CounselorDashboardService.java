package com.example.demo.service;

import com.example.demo.dto.CounselorDashboardStatsResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CounselorDashboardService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CounselingSessionRepository sessionRepository;
    private final CareerPlanRepository careerPlanRepository;
    private final ResourceRepository resourceRepository;

    public CounselorDashboardService(UserRepository userRepository,
                                     StudentRepository studentRepository,
                                     CounselingSessionRepository sessionRepository,
                                     CareerPlanRepository careerPlanRepository,
                                     ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.sessionRepository = sessionRepository;
        this.careerPlanRepository = careerPlanRepository;
        this.resourceRepository = resourceRepository;
    }

    public CounselorDashboardStatsResponse getDashboardStats(Long counselorId) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        CounselorDashboardStatsResponse stats = new CounselorDashboardStatsResponse();

        stats.setTotalStudents((long) studentRepository.findByCounselor(counselor).size());
        stats.setActiveStudents((long) studentRepository.findByCounselorAndStatus(counselor, "active").size());
        stats.setTotalSessions(sessionRepository.countByCounselor(counselor));
        stats.setScheduledSessions(sessionRepository.countByCounselorAndStatus(counselor, "scheduled"));
        stats.setTotalCareerPlans(careerPlanRepository.countByCounselor(counselor));
        stats.setActiveCareerPlans(careerPlanRepository.countByCounselorAndStatus(counselor, "active"));
        stats.setTotalResources(resourceRepository.countByCounselor(counselor));
        stats.setFeaturedResources((long) resourceRepository.findByCounselorAndIsFeatured(counselor, true).size());

        return stats;
    }
}
