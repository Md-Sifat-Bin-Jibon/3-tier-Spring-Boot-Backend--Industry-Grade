package com.example.demo.service;

import com.example.demo.dto.ResourceRequest;
import com.example.demo.dto.ResourceResponse;
import com.example.demo.entity.Resource;
import com.example.demo.entity.User;
import com.example.demo.repository.ResourceRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ResourceService(ResourceRepository resourceRepository,
                          UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    public ResourceResponse createResource(Long counselorId, ResourceRequest request) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        Resource resource = new Resource();
        resource.setCounselor(counselor);
        resource.setTitle(request.getTitle());
        resource.setDescription(request.getDescription());
        resource.setType(request.getType());
        resource.setResourceUrl(request.getResourceUrl());
        resource.setCategory(request.getCategory());
        resource.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);

        resource = resourceRepository.save(resource);
        return mapToResponse(resource);
    }

    public ResourceResponse updateResource(Long counselorId, Long resourceId, ResourceRequest request) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (!resource.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to update this resource");
        }

        resource.setTitle(request.getTitle());
        resource.setDescription(request.getDescription());
        resource.setType(request.getType());
        resource.setResourceUrl(request.getResourceUrl());
        resource.setCategory(request.getCategory());
        if (request.getIsFeatured() != null) {
            resource.setIsFeatured(request.getIsFeatured());
        }

        resource = resourceRepository.save(resource);
        return mapToResponse(resource);
    }

    public void deleteResource(Long counselorId, Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (!resource.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to delete this resource");
        }

        resourceRepository.delete(resource);
    }

    public List<ResourceResponse> getResources(Long counselorId) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return resourceRepository.findByCounselor(counselor).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ResourceResponse> getResourcesByType(Long counselorId, String type) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return resourceRepository.findByCounselorAndType(counselor, type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ResourceResponse> getFeaturedResources(Long counselorId) {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        return resourceRepository.findByCounselorAndIsFeatured(counselor, true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ResourceResponse getResourceById(Long counselorId, Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (!resource.getCounselor().getId().equals(counselorId)) {
            throw new RuntimeException("You don't have permission to view this resource");
        }

        return mapToResponse(resource);
    }

    private ResourceResponse mapToResponse(Resource resource) {
        ResourceResponse response = new ResourceResponse();
        response.setId(resource.getId());
        response.setTitle(resource.getTitle());
        response.setDescription(resource.getDescription());
        response.setType(resource.getType());
        response.setResourceUrl(resource.getResourceUrl());
        response.setCategory(resource.getCategory());
        response.setIsFeatured(resource.getIsFeatured());
        response.setCreatedAt(resource.getCreatedAt());
        response.setUpdatedAt(resource.getUpdatedAt());
        return response;
    }
}
