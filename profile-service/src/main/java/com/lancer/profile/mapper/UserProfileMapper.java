package com.lancer.profile.mapper;

import org.mapstruct.Mapper;

import com.lancer.profile.dto.request.ProfileCreationRequest;
import com.lancer.profile.dto.response.UserProfileResponse;
import com.lancer.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile entity);
}
