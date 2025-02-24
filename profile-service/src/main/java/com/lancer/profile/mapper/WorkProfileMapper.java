package com.lancer.profile.mapper;

import com.lancer.profile.dto.request.ProfileCreationRequest;
import com.lancer.profile.dto.request.WorkProfileRequest;
import com.lancer.profile.dto.request.WorkProfileUpdateRequest;
import com.lancer.profile.dto.response.UserProfileResponse;
import com.lancer.profile.dto.response.WorkProfileResponse;
import com.lancer.profile.entity.UserProfile;
import com.lancer.profile.entity.WorkProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkProfileMapper {
    WorkProfile toWorkProfile(WorkProfileRequest request);

    WorkProfileResponse toWorkProfileResponse(WorkProfile entity);
    @Mapping(target = "id", ignore = true)
    void updateWorkProfile(@MappingTarget WorkProfile workProfile, WorkProfileUpdateRequest request);
}
