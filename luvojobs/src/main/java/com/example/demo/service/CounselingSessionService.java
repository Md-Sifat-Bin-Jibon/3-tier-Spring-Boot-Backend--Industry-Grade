package com.example.demo.service;

import com.example.demo.dto.SessionRequest;
import com.example.demo.dto.SessionResponse;
import com.example.demo.entity.CounselingSession;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.CounselingSessionRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CounselingSessionService {

    private final CounselingSessionRepository sessionRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public CounselingSessionService(CounselingSessionRepository sessionRepository,
                                    StudentRepository studentRepository,
                                    UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public SessionResponse createSession(Long counselorId, SessionRequest request) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!student.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to create session for this student");
        }

        CounselingSession session = new CounselingSession();
        session.setStudent(student);
        session.setCounselor(counselor);
        session.setScheduledDate(request.getScheduledDate());
        session.setDurationMinutes(request.getDurationMinutes());
        session.setType(request.getType());
        session.setMeetingLink(request.getMeetingLink());
        session.setLocation(request.getLocation());
        session.setNotes(request.getNotes());
        session.setStatus("scheduled");

        // Update student's last session date
        student.setLastSessionDate(request.getScheduledDate());
        studentRepository.save(student);

        session = sessionRepository.save(session);
        return mapToResponse(session);
    }

    public SessionResponse updateSession(Long counselorId, Long sessionId, SessionRequest request) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to update this session");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        session.setStudent(student);
        session.setScheduledDate(request.getScheduledDate());
        session.setDurationMinutes(request.getDurationMinutes());
        session.setType(request.getType());
        session.setMeetingLink(request.getMeetingLink());
        session.setLocation(request.getLocation());
        session.setNotes(request.getNotes());

        session = sessionRepository.save(session);
        return mapToResponse(session);
    }

    public void deleteSession(Long counselorId, Long sessionId) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to delete this session");
        }

        sessionRepository.delete(session);
    }

    public List<SessionResponse> getSessions(Long counselorId) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return sessionRepository.findByCounselor(counselor).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SessionResponse> getSessionsByStatus(Long counselorId, String status) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return sessionRepository.findByCounselorAndStatus(counselor, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SessionResponse updateSessionStatus(Long counselorId, Long sessionId, String status, String feedback) {
        CounselingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to update this session");
        }

        session.setStatus(status);
        if (feedback != null) {
            session.setFeedback(feedback);
        }

        session = sessionRepository.save(session);
        return mapToResponse(session);
    }

    private SessionResponse mapToResponse(CounselingSession session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setStudentId(session.getStudent().getId());
        response.setStudentName(session.getStudent().getUser().getUsername());
        response.setStudentEmail(session.getStudent().getUser().getEmail());
        response.setScheduledDate(session.getScheduledDate());
        response.setDurationMinutes(session.getDurationMinutes());
        response.setType(session.getType());
        response.setStatus(session.getStatus());
        response.setMeetingLink(session.getMeetingLink());
        response.setLocation(session.getLocation());
        response.setNotes(session.getNotes());
        response.setFeedback(session.getFeedback());
        response.setCreatedAt(session.getCreatedAt());
        response.setUpdatedAt(session.getUpdatedAt());
        return response;
    }
}
