package com.lancer.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lancer.identity.dto.request.UserCreationRequest;
import com.lancer.identity.dto.request.UserUpdateRequest;
import com.lancer.identity.dto.response.UserResponse;
import com.lancer.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
