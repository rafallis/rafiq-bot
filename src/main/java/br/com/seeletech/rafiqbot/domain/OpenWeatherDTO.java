package br.com.seeletech.rafiqbot.domain;

public record OpenWeatherDTO(
        double latitude,
        double longitude,
        int weatherId,
        String weatherIcon,
        double temp,
        double feels,
        int humidity,
        double windSpeed,
        String cityName
) {
}
