package com.project.journalApp.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.journalApp.api.response.WeatherResponse;
import com.project.journalApp.cache.AppCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeather(String city) {
        WeatherResponse response = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(response != null) {
//            log.info("from redis");
            return response;
        }

        String url = appCache.getAppCache().get("weather_api_url");
        url = url.replace("<CITY>", city).replace("<APIKEY>", apiKey);
        ResponseEntity<WeatherResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = responseEntity.getBody();
        if(body != null) {
            redisService.set("weather_of_" + city, body, 300l);
        }
        return body;
    }
}
