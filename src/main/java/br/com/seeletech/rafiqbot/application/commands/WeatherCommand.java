package br.com.seeletech.rafiqbot.application.commands;

import br.com.seeletech.rafiqbot.domain.OpenWeatherConditionsEnum;
import br.com.seeletech.rafiqbot.domain.OpenWeatherDTO;
import br.com.seeletech.rafiqbot.domain.service.OpenMeteoService;
import br.com.seeletech.rafiqbot.domain.service.OpenWeatherService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class WeatherCommand implements SlashCommand {

    private final OpenWeatherService openWeatherService;
    private final OpenMeteoService openMeteoService;

    @Value("${app.bot.ephemeral:}")
    private Boolean isEphemeral;

    public WeatherCommand(OpenWeatherService openWeatherService, OpenMeteoService openMeteoService) {
        this.openWeatherService = openWeatherService;
        this.openMeteoService = openMeteoService;
    }

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) throws Exception {

        OpenWeatherDTO weather = this.openWeatherService.getWeather(
                event.getOption("city_name")
                        .flatMap(ApplicationCommandInteractionOption::getValue)
                        .map(ApplicationCommandInteractionOptionValue::asString)
                        .orElseThrow(() -> new Exception("city name invalid or null")));

        Map<String,String> airQuality = this.openMeteoService.getAirQuality(
                String.valueOf(weather.latitude()),
                String.valueOf(weather.longitude())
        );

        String weatherImage = this.openWeatherService.buildIconUrl(weather.weatherIcon());

        Color color;
        String image;

        if (weather.feels() < 10.0) {
            color = Color.BLUE;
            image = "https://www.digitalmomblog.com/wp-content/uploads/2022/10/funny-cold-weather-memes.jpeg";
        } else if (weather.feels() < 20.0) {
            color = Color.GREEN;
            image = "https://imgix.bustle.com/uploads/image/2023/7/20/14dfabae-fe90-4277-ad9e-a95344137690-poke-sleep.jpg?w=1200&h=630&fit=crop&crop=faces&fm=jpg";
        } else if (weather.feels() < 30.0) {
            color = Color.ORANGE;
            image = "https://pbs.twimg.com/media/EDP_TO3W4AAnCMq.jpg";
        } else {
            color = Color.RED;
            image = "https://assets.gqindia.com/photos/5cdc6a3e8e6299d919f37c9f/16:9/pass/sweating.jpg";
        }

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(color)
                .title(weather.cityName())
                .description("")
                .thumbnail(weatherImage)
                .addField("Temp", String.valueOf(weather.temp()).concat(" °C"), true)
                .addField("Feels", String.valueOf(weather.feels()).concat(" °C"), true)
                .addField("Weather", StringUtils.capitalize(OpenWeatherConditionsEnum.valueOfId(weather.weatherId()).condition), false)
                .addField("CO", String.valueOf(airQuality.get("co")).concat(" μg/m³"), true)
                .addField("SO2", String.valueOf(airQuality.get("so2")).concat(" μg/m³"), true)
                .addField("Lat", String.valueOf(weather.latitude()), true)
                .addField("Long", String.valueOf(weather.longitude()), true)
                .addField("Wind", String.valueOf(weather.windSpeed()).concat(" kmh"), true)
                .addField("Reference", "https://openweathermap.org", false)
                .footer("seeletech.com.br", "https://static.wikia.nocookie.net/evangelion/images/8/8e/SEELE_Logo.png/revision/latest?cb=20120312002542")
                .image(image)
                .build();

        return event.reply()
                .withEphemeral(isEphemeral)
                .withEmbeds(embed);
    }
}
