package br.com.seeletech.rafiqbot.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum WeathercodeEnum {

    CLEAR(0),
    MAINLY_CLEAR(1),
    PARTLY_CLOUDY(2),
    OVERCAST(3),
    FOG(45),
    DEPOSITING_RIME_FOG(48),
    DRIZZLE_LIGHT(51),
    DRIZZLE_MODERATE(53),
    DRIZZLE_DENSE(55),
    FREEZING_DRIZZLE_LIGHT(56),
    FREEZING_DRIZZLE_DENSE(57),
    RAIN_SLIGHT(61),
    RAIN_MODERATE(63),
    RAIN_HEAVY(65),
    FREEZING_RAIN_LIGHT(66),
    FREEZING_RAIN_HEAVY(67),
    SNOW_FALL_SLIGHT(71),
    SNOW_FALL_MODERATE(73),
    SNOW_FALL_HEAVY(75),
    SNOW_GRAINS(77),
    RAIN_SHOWERS_SLIGHT(80),
    RAIN_SHOWERS_MODERATE(81),
    RAIN_SHOWERS_VIOLENT(82),
    SNOW_SHOWERS_SLIGHT(85),
    SNOW_SHOWERS_HEAVY(86),
    THUNDERSTORM(95),
    THUNDERSTORM_SLIGHT_HAIL(96),
    THUNDERSTORM_HEAVY_HAIL(97);

    private final int code;

    private static final Map<Integer, WeathercodeEnum> BY_CODE = new HashMap<>();

    static {
        for (WeathercodeEnum w : values()) {
            BY_CODE.put(w.code, w);
        }
    }

    WeathercodeEnum(int code) {
        this.code = code;
    }

    public static WeathercodeEnum valueOfCode(Integer code) {
        return BY_CODE.get(code);
    }

}
