package com.project.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.journalApp.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
	
	User findByUserName(String userName);

	void deleteByUserName(String userName);
}
