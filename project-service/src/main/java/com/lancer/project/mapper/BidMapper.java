package com.lancer.project.mapper;

import com.lancer.project.dto.request.BidRequest;
import com.lancer.project.dto.response.BidResponse;
import com.lancer.project.entity.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BidMapper {
    Bid toBid(BidRequest request);
    BidResponse toBidResponse(Bid entity);

    @Mapping(target = "id", ignore = true)
    void updateBid(@MappingTarget Bid bid, BidRequest request);
}
