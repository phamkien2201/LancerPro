package com.lancer.identity.mapper;

import org.mapstruct.Mapper;

import com.lancer.identity.dto.request.PermissionRequest;
import com.lancer.identity.dto.response.PermissionResponse;
import com.lancer.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
