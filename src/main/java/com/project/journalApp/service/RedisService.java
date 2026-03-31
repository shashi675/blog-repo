package com.project.journalApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.journalApp.api.response.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    Logger log = LoggerFactory.getLogger(RedisService.class);

    public <T> T get(String key, Class<T> entityClass) {
        try {
//            log.info("log value:{}", redisTemplate.opsForValue().get("name"));

            Object rawValue = redisTemplate.opsForValue().get(key);
            if(rawValue == null) return null;
            return objectMapper.readValue(rawValue.toString(), entityClass);
        }
        catch (Exception e) {
            log.error("exception: ", e);
            return null;
        }
    }

    public void set(String key, Object body, long timeToLiveInSeconds) {
        try {
            String toWrite = objectMapper.writeValueAsString(body);
            redisTemplate.opsForValue().set(key, toWrite, timeToLiveInSeconds, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            log.error("exception: ", e);
        }
    }
}
