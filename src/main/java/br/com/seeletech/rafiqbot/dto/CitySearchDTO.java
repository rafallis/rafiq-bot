package br.com.seeletech.rafiqbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CitySearchDTO {


    private List<ResponseObject> responseObject;

    @Getter
    @Setter
    public static class ResponseObject {
        @JsonProperty("Version")
        private Integer version;
        @JsonProperty("Key")
        private String key;
    //    private String type;
    //    private Integer rank;
    //    private String localizedName;
    //    private Region region;
    //    private Country country;
    //    private AdministrativeArea administrativeArea;

    }

    @Getter
    @Setter
    public static class Region {
        private String id;
        private String localizedName;
        private String englishName;
    }

    @Getter
    @Setter
    public static class Country {
        private String id;
        private String localizedName;
        private String englishName;
    }

    @Getter
    @Setter
    public static class AdministrativeArea {
        private String id;
        private String localizedName;
        private String englishName;
        private Integer level;
        private String localizedType;
        private String englishType;
        private String countryID;
    }

}
