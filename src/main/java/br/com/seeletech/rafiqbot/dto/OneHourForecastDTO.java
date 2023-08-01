package br.com.seeletech.rafiqbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OneHourForecastDTO {

    private LocalDateTime dateTime;
    private String iconPhrase;
    private boolean hasPrecipitation;
    private boolean isDaylight;
    private Temparature temparature;
    private int precipitationProbability;
    private String link;

    @Getter
    @Setter
    public static class Temparature {
        private Double value;
        private String unit;
    }
}
