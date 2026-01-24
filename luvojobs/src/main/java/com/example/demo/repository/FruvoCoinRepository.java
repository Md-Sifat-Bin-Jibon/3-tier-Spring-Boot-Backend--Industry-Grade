package com.example.demo.repository;

import com.example.demo.entity.FruvoCoin;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FruvoCoinRepository extends JpaRepository<FruvoCoin, Long> {
    Optional<FruvoCoin> findByUser(User user);
    Optional<FruvoCoin> findByUserId(Long userId);
    boolean existsByUser(User user);
}
