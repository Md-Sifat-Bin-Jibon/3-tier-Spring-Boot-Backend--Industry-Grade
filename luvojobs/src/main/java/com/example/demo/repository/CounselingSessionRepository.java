package com.example.demo.repository;

import com.example.demo.entity.CounselingSession;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CounselingSessionRepository extends JpaRepository<CounselingSession, Long> {
    List<CounselingSession> findByCounselor(User counselor);
    List<CounselingSession> findByStudent(Student student);
    List<CounselingSession> findByCounselorAndStatus(User counselor, String status);
    List<CounselingSession> findByCounselorAndScheduledDateAfter(User counselor, LocalDateTime date);
    long countByCounselor(User counselor);
    long countByCounselorAndStatus(User counselor, String status);
}
