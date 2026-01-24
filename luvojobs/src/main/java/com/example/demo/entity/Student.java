package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; // Link to User entity

    @ManyToOne
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor; // The career counselor managing this student

    @Column(nullable = false)
    private String program; // e.g., Computer Science, Business Administration

    @Column(nullable = false)
    private String year; // e.g., 1st Year, 2nd Year, 3rd Year, 4th Year

    @Column(name = "gpa")
    private String gpa;

    @Column(nullable = false)
    private String status = "active"; // active, inactive

    @Column(name = "last_session_date")
    private LocalDateTime lastSessionDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Student() {}

    public Student(User user, User counselor, String program, String year) {
        this.user = user;
        this.counselor = counselor;
        this.program = program;
        this.year = year;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCounselor() {
        return counselor;
    }

    public void setCounselor(User counselor) {
        this.counselor = counselor;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastSessionDate() {
        return lastSessionDate;
    }

    public void setLastSessionDate(LocalDateTime lastSessionDate) {
        this.lastSessionDate = lastSessionDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
