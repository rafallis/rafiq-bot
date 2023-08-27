package br.com.seeletech.rafiqbot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
public class StockData {

    public Meta meta;
    public ArrayList<Datum> data;

    @Getter
    @Setter
    public static class Meta {
        public int requested;
        public int returned;
    }

    @Getter
    @Setter
    public static class Datum {
        public String ticker;
        public String name;
        public Object exchange_short;
        public Object exchange_long;
        public String mic_code;
        public String currency;
        public double price;
        public double day_high;
        public double day_low;
        public double day_open;
        @JsonProperty("52_week_high")
        public double _52_week_high;
        @JsonProperty("52_week_low")
        public double _52_week_low;
        public Object market_cap;
        public double previous_close_price;
        public Date previous_close_price_time;
        public double day_change;
        public int volume;
        public boolean is_extended_hours_price;
        public Date last_trade_time;
    }

}
