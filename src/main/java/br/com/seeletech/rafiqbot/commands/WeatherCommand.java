package br.com.seeletech.rafiqbot.commands;

import br.com.seeletech.rafiqbot.dto.CurrentWeatherDTO;
import br.com.seeletech.rafiqbot.model.enums.WeathercodeEnum;
import br.com.seeletech.rafiqbot.service.LoremPokemonService;
import br.com.seeletech.rafiqbot.service.OpenMeteoService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Component
public class WeatherCommand implements SlashCommand {

    private final OpenMeteoService openMeteoService;
    private final LoremPokemonService loremPokemonService;

    @Value("${app.bot.ephemeral:}")
    private Boolean isEphemeral;

    public WeatherCommand(OpenMeteoService openMeteoService, LoremPokemonService loremPokemonService) {
        this.openMeteoService = openMeteoService;
        this.loremPokemonService = loremPokemonService;
    }

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String cityName = event.getOption("city_name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();

        CurrentWeatherDTO forecast = this.openMeteoService.getForecast(cityName);
        double temperature = forecast.getCurrent_weather().getTemperature();
        WeathercodeEnum currentWeather = WeathercodeEnum.valueOfCode(forecast.getCurrent_weather().getWeathercode());

//        String randomPokemonUri = this.loremPokemonService.getRandomPokemonUrl();
        Color color;
        String image;

        if (temperature < 10.0) {
            color = Color.BLUE;
            image = "https://external-preview.redd.it/T7w78aEU-vj1kVjIlbD5FGiu_NpEuIU0qRNE9VWDT78.jpg";
        } else if (temperature < 20.0) {
            color = Color.GREEN;
            image = "https://imgix.bustle.com/uploads/image/2023/7/20/14dfabae-fe90-4277-ad9e-a95344137690-poke-sleep.jpg?w=1200&h=630&fit=crop&crop=faces&fm=jpg";
        } else if (temperature < 30.0) {
            color = Color.ORANGE;
            image = "https://pbs.twimg.com/media/EDP_TO3W4AAnCMq.jpg";
        } else {
            color = Color.RED;
            image = "https://assets.gqindia.com/photos/5cdc6a3e8e6299d919f37c9f/16:9/pass/sweating.jpg";
        }

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(color)
                .title(forecast.getCityName())
                .description("")
                .thumbnail(getCurrentWeatherImage(currentWeather))
                .addField("State", String.valueOf(forecast.getState()), true)
                .addField("Country", String.valueOf(forecast.getCountry()), true)
                .addField("Temp", String.valueOf(forecast.getCurrent_weather().getTemperature()).concat(" °C"), true)
                .addField("Weather", StringUtils.capitalize(WeathercodeEnum.valueOfCode(currentWeather.getCode()).toString().toLowerCase().replace("_", " ")), true)
                .addField("CO", String.valueOf(forecast.getCarbon_monoxide()).concat(" μg/m³"), true)
                .addField("SO2", String.valueOf(forecast.getSuphur_dioxide()).concat(" μg/m³"), true)
                .addField("Lat", String.valueOf(forecast.getLongitude()), true)
                .addField("Long", String.valueOf(forecast.getLatitude()), true)
                .addField("Wind", String.valueOf(forecast.getCurrent_weather().getWindspeed()).concat(" kmh"), true)
                .addField("Reference", "https://open-meteo.com/", false)
                .footer("seeletech.com.br", "https://static.wikia.nocookie.net/evangelion/images/8/8e/SEELE_Logo.png/revision/latest?cb=20120312002542")
                .image(image)
                .build();

        return event.reply()
                .withEphemeral(isEphemeral)
                .withEmbeds(embed);
    }

    private String getCurrentWeatherImage(WeathercodeEnum currentWeather) {
        switch (currentWeather) {
            case CLEAR -> {
                return "https://cdn2.iconfinder.com/data/icons/weather-flat-14/64/weather01-512.png";
            }
            case MAINLY_CLEAR, PARTLY_CLOUDY, OVERCAST -> {
                return "https://cdn2.iconfinder.com/data/icons/weather-flat-14/64/weather02-512.png";
            }
            case FOG, DEPOSITING_RIME_FOG -> {
                return "https://img.myloview.com.br/posters/fog-glyph-icon-weather-and-forecast-humidity-sign-vector-graphics-a-solid-pattern-on-a-white-background-700-166768715.jpg";
            }
            case DRIZZLE_LIGHT, RAIN_SLIGHT, RAIN_SHOWERS_SLIGHT -> {
                return "https://cdn2.iconfinder.com/data/icons/weather-flat-14/64/weather06-512.png";
            }
            case DRIZZLE_MODERATE, RAIN_MODERATE, RAIN_SHOWERS_MODERATE -> {
                return "https://cdn2.iconfinder.com/data/icons/weather-flat-14/64/weather07-512.png";
            }
            case DRIZZLE_DENSE, RAIN_HEAVY, RAIN_SHOWERS_VIOLENT, THUNDERSTORM, THUNDERSTORM_SLIGHT_HAIL, THUNDERSTORM_HEAVY_HAIL -> {
                return "https://cdn2.iconfinder.com/data/icons/weather-flat-14/64/weather08-512.png";
            }
            case FREEZING_DRIZZLE_LIGHT, FREEZING_RAIN_LIGHT, SNOW_FALL_SLIGHT, SNOW_SHOWERS_SLIGHT, SNOW_GRAINS -> {
                return "https://cdn2.iconfinder.com/data/icons/weather-flat-14/64/weather12-512.png";
            }
            case FREEZING_DRIZZLE_DENSE, FREEZING_RAIN_HEAVY, SNOW_FALL_MODERATE, SNOW_FALL_HEAVY, SNOW_SHOWERS_HEAVY -> {
                return "https://cdn2.iconfinder.com/data/icons/weather-flat-14/64/weather13-512.png";
            }
            default -> {
                return "https://cdn0.iconfinder.com/data/icons/fitness-95/24/meditation-512.png";
            }
        }
    }
}
