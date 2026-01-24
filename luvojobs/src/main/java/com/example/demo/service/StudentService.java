package com.example.demo.service;

import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.CareerPlanRepository;
import com.example.demo.repository.CounselingSessionRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final CounselingSessionRepository sessionRepository;
    private final CareerPlanRepository careerPlanRepository;

    public StudentService(StudentRepository studentRepository,
                         UserRepository userRepository,
                         CounselingSessionRepository sessionRepository,
                         CareerPlanRepository careerPlanRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.careerPlanRepository = careerPlanRepository;
    }

    public List<StudentResponse> getStudents(Long counselorId) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return studentRepository.findByCounselor(counselor).stream()
                .map(student -> mapToResponse(student))
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentsByStatus(Long counselorId, String status) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return studentRepository.findByCounselorAndStatus(counselor, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(Long counselorId, Long studentId) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!student.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to view this student");
        }

        return mapToResponse(student);
    }

    private StudentResponse mapToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setUserId(student.getUser().getId());
        response.setName(student.getUser().getUsername());
        response.setEmail(student.getUser().getEmail());
        response.setPhone(null); // Phone not available in User entity
        response.setProgram(student.getProgram());
        response.setYear(student.getYear());
        response.setGpa(student.getGpa());
        response.setStatus(student.getStatus());
        response.setLastSessionDate(student.getLastSessionDate());

        // Get session count
        long sessionCount = sessionRepository.findByStudent(student).size();
        response.setSessionsCount(sessionCount);

        // Check if has career plan
        boolean hasCareerPlan = careerPlanRepository.findByStudent(student).size() > 0;
        response.setHasCareerPlan(hasCareerPlan);

        return response;
    }
}
