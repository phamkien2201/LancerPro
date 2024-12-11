package com.studytracker.profile.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.studytracker.profile.dto.request.ProfileCreationRequest;
import com.studytracker.profile.dto.response.UserProfileResponse;
import com.studytracker.profile.entity.UserProfile;
import com.studytracker.profile.mapper.UserProfileMapper;
import com.studytracker.profile.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String userId) {
        return userProfileMapper.toUserProfileResponse(userProfileRepository.findByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        var profiles = userProfileRepository.findAll();
        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }
}
