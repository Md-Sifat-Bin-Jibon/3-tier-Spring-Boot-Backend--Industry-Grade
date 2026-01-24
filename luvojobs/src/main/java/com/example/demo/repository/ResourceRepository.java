package com.example.demo.repository;

import com.example.demo.entity.Resource;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByCounselor(User counselor);
    List<Resource> findByCounselorAndType(User counselor, String type);
    List<Resource> findByCounselorAndCategory(User counselor, String category);
    List<Resource> findByCounselorAndIsFeatured(User counselor, Boolean isFeatured);
    long countByCounselor(User counselor);
}
