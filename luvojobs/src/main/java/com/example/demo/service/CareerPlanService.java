package com.example.demo.service;

import com.example.demo.dto.CareerPlanRequest;
import com.example.demo.dto.CareerPlanResponse;
import com.example.demo.entity.CareerPlan;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.CareerPlanRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CareerPlanService {

    private final CareerPlanRepository careerPlanRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public CareerPlanService(CareerPlanRepository careerPlanRepository,
                            StudentRepository studentRepository,
                            UserRepository userRepository) {
        this.careerPlanRepository = careerPlanRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public CareerPlanResponse createCareerPlan(Long counselorId, CareerPlanRequest request) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!student.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to create career plan for this student");
        }

        CareerPlan plan = new CareerPlan();
        plan.setStudent(student);
        plan.setCounselor(counselor);
        plan.setTitle(request.getTitle());
        plan.setTimeline(request.getTimeline());
        plan.setGoals(request.getGoals() != null ? request.getGoals() : new java.util.ArrayList<>());
        plan.setActionItems(request.getActionItems() != null ? request.getActionItems() : new java.util.ArrayList<>());
        plan.setStatus(request.getStatus() != null ? request.getStatus() : "draft");

        plan = careerPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public CareerPlanResponse updateCareerPlan(Long counselorId, Long planId, CareerPlanRequest request) {
        CareerPlan plan = careerPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Career plan not found"));

        if (!plan.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to update this career plan");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        plan.setStudent(student);
        plan.setTitle(request.getTitle());
        plan.setTimeline(request.getTimeline());
        plan.setGoals(request.getGoals() != null ? request.getGoals() : new java.util.ArrayList<>());
        plan.setActionItems(request.getActionItems() != null ? request.getActionItems() : new java.util.ArrayList<>());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        plan = careerPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public void deleteCareerPlan(Long counselorId, Long planId) {
        CareerPlan plan = careerPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Career plan not found"));

        if (!plan.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to delete this career plan");
        }

        careerPlanRepository.delete(plan);
    }

    public List<CareerPlanResponse> getCareerPlans(Long counselorId) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return careerPlanRepository.findByCounselor(counselor).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CareerPlanResponse> getCareerPlansByStatus(Long counselorId, String status) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return careerPlanRepository.findByCounselorAndStatus(counselor, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CareerPlanResponse getCareerPlanById(Long counselorId, Long planId) {
        CareerPlan plan = careerPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Career plan not found"));

        if (!plan.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to view this career plan");
        }

        return mapToResponse(plan);
    }

    private CareerPlanResponse mapToResponse(CareerPlan plan) {
        CareerPlanResponse response = new CareerPlanResponse();
        response.setId(plan.getId());
        response.setStudentId(plan.getStudent().getId());
        response.setStudentName(plan.getStudent().getUser().getUsername());
        response.setStudentEmail(plan.getStudent().getUser().getEmail());
        response.setTitle(plan.getTitle());
        response.setGoals(plan.getGoals());
        response.setTimeline(plan.getTimeline());
        response.setActionItems(plan.getActionItems());
        response.setStatus(plan.getStatus());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}
