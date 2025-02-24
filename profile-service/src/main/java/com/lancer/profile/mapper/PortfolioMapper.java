package com.lancer.profile.mapper;

import com.lancer.profile.dto.request.PortfolioRequest;
import com.lancer.profile.dto.request.PortfolioUpdateRequest;
import com.lancer.profile.dto.request.ProfileCreationRequest;
import com.lancer.profile.dto.request.WorkProfileUpdateRequest;
import com.lancer.profile.dto.response.PortfolioResponse;
import com.lancer.profile.dto.response.UserProfileResponse;
import com.lancer.profile.entity.Portfolio;
import com.lancer.profile.entity.UserProfile;
import com.lancer.profile.entity.WorkProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {
    Portfolio toPortfolio(PortfolioRequest request);
    PortfolioResponse toPortfolioResponse(Portfolio entity);
    @Mapping(target = "id", ignore = true)
    void updatePortfolio(@MappingTarget Portfolio portfolio, PortfolioUpdateRequest request);
}
