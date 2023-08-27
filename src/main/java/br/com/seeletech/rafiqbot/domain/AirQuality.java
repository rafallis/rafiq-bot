package br.com.seeletech.rafiqbot.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AirQuality {

    private double latitude;
    private double longitude;
    private double generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private HourlyUnits hourlyUnits;
    private Hourly hourly;
    private String cityName;
    private String state;
    private String country;

    @Data
    public static class HourlyUnits {
        private String time;
        private String carbon_monoxide;
        private String sulphur_dioxide;
    }

    @Data
    public static class Hourly {
        List<LocalDateTime> time;
        List<Double> carbon_monoxide;
        List<Double> sulphur_dioxide;
    }
}
