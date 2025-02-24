package com.lancer.profile.repository;

import com.lancer.profile.entity.UserProfile;
import com.lancer.profile.entity.WorkProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProfileRepository extends MongoRepository<WorkProfile, String> {
    WorkProfile findByUserId(String userId);
}
