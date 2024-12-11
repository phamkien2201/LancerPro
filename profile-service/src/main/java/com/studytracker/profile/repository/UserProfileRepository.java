package com.studytracker.profile.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.studytracker.profile.entity.UserProfile;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findByUserId(String userId);
}
