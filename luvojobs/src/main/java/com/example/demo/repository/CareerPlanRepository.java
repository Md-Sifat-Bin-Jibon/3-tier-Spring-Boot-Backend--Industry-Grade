package com.example.demo.repository;

import com.example.demo.entity.CareerPlan;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerPlanRepository extends JpaRepository<CareerPlan, Long> {
    List<CareerPlan> findByCounselor(User counselor);
    List<CareerPlan> findByStudent(Student student);
    List<CareerPlan> findByCounselorAndStatus(User counselor, String status);
    Optional<CareerPlan> findByStudentAndCounselor(Student student, User counselor);
    long countByCounselor(User counselor);
    long countByCounselorAndStatus(User counselor, String status);
}
