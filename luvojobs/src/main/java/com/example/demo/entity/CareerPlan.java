package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "career_plans")
public class CareerPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @CollectionTable(name = "career_plan_goals", joinColumns = @JoinColumn(name = "career_plan_id"))
    @Column(name = "goal", columnDefinition = "TEXT")
    private List<String> goals = new ArrayList<>();

    @Column(nullable = false)
    private String timeline; // e.g., "6 months", "1 year"

    @ElementCollection
    @CollectionTable(name = "career_plan_action_items", joinColumns = @JoinColumn(name = "career_plan_id"))
    @Column(name = "action_item", columnDefinition = "TEXT")
    private List<String> actionItems = new ArrayList<>();

    @Column(nullable = false)
    private String status = "draft"; // draft, active, completed

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public CareerPlan() {}

    public CareerPlan(Student student, User counselor, String title, String timeline) {
        this.student = student;
        this.counselor = counselor;
        this.title = title;
        this.timeline = timeline;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public User getCounselor() {
        return counselor;
    }

    public void setCounselor(User counselor) {
        this.counselor = counselor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public List<String> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<String> actionItems) {
        this.actionItems = actionItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
