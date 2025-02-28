package com.lancer.bid.repository;

import com.lancer.bid.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BidRepository extends MongoRepository<Bid, String> {
    Page<Bid> findByProjectId(String projectId, PageRequest pageRequest);
    Page<Bid> findByFreelancerId(String freelancerId, PageRequest pageRequest);
}
