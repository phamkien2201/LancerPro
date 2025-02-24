package com.lancer.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lancer.identity.dto.request.RoleRequest;
import com.lancer.identity.dto.response.RoleResponse;
import com.lancer.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
