package com.lancer.profile.repository;

import com.lancer.profile.entity.Portfolio;
import com.lancer.profile.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
    Portfolio findByUserId(String userId);
}
