package br.com.seeletech.rafiqbot.domain;

import java.util.HashMap;
import java.util.Map;

public enum OpenWeatherConditionsEnum {

    // THUNDERSTORM
    THUNDERSTORM0("thunderstorm with light rain", 200),
    THUNDERSTORM1("thunderstorm with rain", 201),
    THUNDERSTORM2("thunderstorm with heavy rain", 202),
    THUNDERSTORM3("light thunderstorm", 210),
    THUNDERSTORM4("thunderstorm", 211),
    THUNDERSTORM5("heavy thunderstorm", 212),
    THUNDERSTORM6("ragged thunderstorm", 221),
    THUNDERSTORM7("thunderstorm with light drizzle", 230),
    THUNDERSTORM8("thunderstorm with drizzle", 231),
    THUNDERSTORM9("thunderstorm with heavy drizzle", 232),

    // DRIZZLE
    DRIZZLE0("light intensity drizzle", 300),
    DRIZZLE1("drizzle", 301),
    DRIZZLE2("heavy intensity drizzle", 302),
    DRIZZLE3("light intensity drizzle rain", 310),
    DRIZZLE4("drizzle rain", 311),
    DRIZZLE5("heavy intensity drizzle rain", 312),
    DRIZZLE6("shower rain and drizzle", 313),
    DRIZZLE7("heavy shower rain and drizzle", 314),
    DRIZZLE8("shower drizzle", 321),

    // RAIN
    RAIN0("light rain", 500),
    RAIN1("moderate rain", 501),
    RAIN2("heavy intensity rain", 502),
    RAIN3("very heavy rain", 503),
    RAIN4("extreme rain", 504),
    RAIN5("freezing rain", 511),
    RAIN6("light intensity shower rain", 520),
    RAIN7("shower rain", 521),
    RAIN8("heavy intensity shower rain", 522),
    RAIN9("ragged shower rain", 531),

    // SNOW
    SNOW0("light snow", 600),
    SNOW1("snow", 601),
    SNOW2("heavy snow", 602),
    SNOW3("sleet", 611),
    SNOW4("light shower sleet", 612),
    SNOW5("shower sleet", 613),
    SNOW6("light rain and snow", 615),
    SNOW7("rain and snow", 616),
    SNOW8("light shower snow", 620),
    SNOW9("shower snow", 621),
    SNOW10("heavy shower snow", 622),

    // ATMOSPHERE
    MIST("mist", 701),
    SMOKE("smoke", 711),
    HAZE("haze", 721),
    DUST0("sand/dust whirls", 731),
    FOG("fog", 741),
    SAND("sand", 751),
    DUST1("dust", 761),
    ASH("volcanic ash", 762),
    SQUALL("squalls", 771),
    TORNADO("tornado", 781),

    // CLEAR
    CLEAR("clear sky", 800),

    // CLOUDS
    CLOUDS0("few clouds: 11-25%", 801),
    CLOUDS1("scattered clouds: 25-50%", 802),
    CLOUDS2("broken clouds: 51-84%", 803),
    CLOUDS3("overcast clouds: 85-100%", 804);


    public static final Map<Integer, OpenWeatherConditionsEnum> BY_ID = new HashMap<>();

    static {
        for (OpenWeatherConditionsEnum o : values()) {
            BY_ID.put(o.id, o);
        }
    }

    public final String condition;
    public final int id;

    OpenWeatherConditionsEnum(String condition, int id) {
        this.condition = condition;
        this.id = id;
    }

    public static OpenWeatherConditionsEnum valueOfId(Integer id) {
        return BY_ID.get(id);
    }
}
