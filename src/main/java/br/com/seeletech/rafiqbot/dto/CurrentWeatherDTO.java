package br.com.seeletech.rafiqbot.dto;

import lombok.*;

@Data
public class CurrentWeatherDTO {

    public double latitude;
    public double longitude;
    public double generationtime_ms;
    public int utc_offset_seconds;
    public String timezone;
    public String timezone_abbreviation;
    public double elevation;
    public CurrentWeather current_weather;
    public String cityName;
    public String state;
    public String country;

    @Data
    public static class CurrentWeather {
        public double temperature;
        public double windspeed;
        public double winddirection;
        public int weathercode;
        public int is_day;
        public String time;
    }
}
