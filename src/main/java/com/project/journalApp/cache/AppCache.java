package com.project.journalApp.cache;

import com.project.journalApp.entity.JournalConfig;
import com.project.journalApp.repository.JournalConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class AppCache {

    @Autowired
    JournalConfigRepository journalConfigRepository;

    @Getter
    private HashMap<String, String> appCache = new HashMap<>();

    @PostConstruct
    public void init() {
        List<JournalConfig> allConfigs = journalConfigRepository.findAll();
        for (JournalConfig allConfig : allConfigs) {
            appCache.put(allConfig.getKey(), allConfig.getValue());
        }
    }
}

