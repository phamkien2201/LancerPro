package com.lancer.bid.mapper;

import com.lancer.bid.dto.request.BidRequest;
import com.lancer.bid.dto.response.BidResponse;
import com.lancer.bid.entity.Bid;
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
