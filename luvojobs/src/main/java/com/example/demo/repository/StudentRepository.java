package com.example.demo.repository;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCounselor(User counselor);
    List<Student> findByCounselorAndStatus(User counselor, String status);
    Optional<Student> findByUser(User user);
    Optional<Student> findByUserAndCounselor(User user, User counselor);
    boolean existsByUserAndCounselor(User user, User counselor);
}
