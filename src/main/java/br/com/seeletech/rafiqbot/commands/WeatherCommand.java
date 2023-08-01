package br.com.seeletech.rafiqbot.commands;

import br.com.seeletech.rafiqbot.dto.CurrentWeatherDTO;
import br.com.seeletech.rafiqbot.service.LoremPokemonService;
import br.com.seeletech.rafiqbot.service.OpenMeteoService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class WeatherCommand implements SlashCommand {

    private final OpenMeteoService openMeteoService;
    private final LoremPokemonService loremPokemonService;

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

        CurrentWeatherDTO temp = this.openMeteoService.getForecast(cityName);
        double temperature = temp.current_weather.temperature;
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
                .title(temp.getCityName())
                .description("")
//                .thumbnail(randomPokemonUri)
                .addField("Estado", String.valueOf(temp.state), true)
                .addField("País", String.valueOf(temp.country), true)
                .addField("Temperatura", String.valueOf(temp.current_weather.temperature).concat(" °C"), false)
                .addField("Velocidade do Vento", String.valueOf(temp.current_weather.windspeed).concat(" kmh"), false)
                .addField("Latitude", String.valueOf(temp.longitude), true)
                .addField("Longitude", String.valueOf(temp.latitude), true)
                .footer("seeletech.com.br", "https://static.wikia.nocookie.net/evangelion/images/8/8e/SEELE_Logo.png/revision/latest?cb=20120312002542")
                .image(image)
                .build();

        return event.reply()
                .withEmbeds(embed);
    }
}
