package com.lancer.identity.mapper;

import org.mapstruct.Mapper;

import com.lancer.identity.dto.request.ProfileCreationRequest;
import com.lancer.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
