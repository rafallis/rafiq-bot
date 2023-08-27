package br.com.seeletech.rafiqbot.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OpenWeatherMapper {

    OpenWeatherMapper INSTANCE = Mappers.getMapper(OpenWeatherMapper.class);

    @Mappings({
            @Mapping(source = "coord.lat", target = "latitude"),
            @Mapping(source = "coord.lon", target = "longitude"),
            @Mapping(expression = "java(openWeather.getWeather().get(0).getId())", target = "weatherId"),
            @Mapping(expression = "java(openWeather.getWeather().get(0).getIcon())", target = "weatherIcon"),
            @Mapping(source = "main.temp", target = "temp"),
            @Mapping(source = "main.feels_like", target = "feels"),
            @Mapping(source = "main.humidity", target = "humidity"),
            @Mapping(source = "wind.speed", target = "windSpeed"),
            @Mapping(source = "name", target = "cityName")
    })
    OpenWeatherDTO convert(OpenWeather openWeather);
}
