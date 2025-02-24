package com.lancer.profile.service;

import com.lancer.profile.dto.request.WorkProfileRequest;
import com.lancer.profile.dto.request.WorkProfileUpdateRequest;
import com.lancer.profile.dto.response.WorkProfileResponse;
import com.lancer.profile.entity.WorkProfile;
import com.lancer.profile.exception.ErrorCode;
import com.lancer.profile.exception.AppException;
import com.lancer.profile.mapper.WorkProfileMapper;
import com.lancer.profile.repository.WorkProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WorkProfileService {
    WorkProfileRepository workProfileRepository;
    WorkProfileMapper workProfileMapper;

    public WorkProfileResponse createWorkProfile(WorkProfileRequest request) {
        WorkProfile workProfile = workProfileMapper.toWorkProfile(request);
        workProfile = workProfileRepository.save(workProfile);
        return workProfileMapper.toWorkProfileResponse(workProfile);
    }

    public WorkProfileResponse getWorkProfile(String userId) {
        return workProfileMapper.toWorkProfileResponse(workProfileRepository.findByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<WorkProfileResponse> getAllWorkProfiles() {
        var profiles = workProfileRepository.findAll();
        return profiles.stream().map(workProfileMapper::toWorkProfileResponse).toList();
    }

    public WorkProfileResponse updateWorkProfile(String userId, WorkProfileUpdateRequest request) {
        WorkProfile workProfile = workProfileRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        workProfileMapper.updateWorkProfile(workProfile, request);
        return workProfileMapper.toWorkProfileResponse(workProfileRepository.save(workProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWorkProfile(String id) {
        workProfileRepository.findById(id).ifPresent(workProfileRepository::delete);
    }

}
