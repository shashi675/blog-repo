package com.project.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class WeatherResponse {
    private List<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private String name;
    private int cod;

    @Setter
    @Getter
//    @NoArgsConstructor
    public static class Main {
        private double temp;
        @JsonProperty("feels_like")
        private double feelsLike;
        @JsonProperty("temp_min")
        private double tempMin;
    }

    @Setter
    @Getter
//    @NoArgsConstructor
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }
}
