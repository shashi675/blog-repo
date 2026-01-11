package com.project.journalApp.repository;

import com.project.journalApp.entity.JournalConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalConfigRepository extends MongoRepository<JournalConfig, Object> {
}
