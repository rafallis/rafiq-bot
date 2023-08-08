package br.com.seeletech.rafiqbot.dto;

import lombok.*;

@Data
public class CurrentWeatherDTO {

    private double latitude;
    private double longitude;
    private double generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private double elevation;
    private CurrentWeather current_weather;
    private String cityName;
    private String state;
    private String country;
    private Double carbon_monoxide;
    private Double suphur_dioxide;

    @Data
    public static class CurrentWeather {
        private double temperature;
        private double windspeed;
        private double winddirection;
        private int weathercode;
        private int is_day;
        private String time;
    }
}
