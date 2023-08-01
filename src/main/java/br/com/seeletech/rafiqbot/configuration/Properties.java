package br.com.seeletech.rafiqbot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@Component
public class Properties {

    private Accuweather accuweather;
    private OpenMeteo openMeteo;
    private LoremPokemon loremPokemon;
    private Bot bot;

    @Getter
    @Setter
    public static class Accuweather {
        private String urlLocations;
        private String urlForecasts;
        private String apikey;
    }

    @Getter
    @Setter
    public static class OpenMeteo {
        private String urlGeocoding;
        private String urlForecast;
    }

    @Getter
    @Setter
    public static class LoremPokemon {
        private String url;
    }

    @Getter
    @Setter
    public static class Bot {
        private String token;
    }
}
