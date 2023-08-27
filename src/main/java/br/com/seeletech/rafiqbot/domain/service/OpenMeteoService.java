package br.com.seeletech.rafiqbot.domain.service;

import br.com.seeletech.rafiqbot.configuration.Properties;
import br.com.seeletech.rafiqbot.dto.AirQualityDTO;
import br.com.seeletech.rafiqbot.dto.CurrentWeatherDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
public class OpenMeteoService {

    private final RestTemplate restTemplate;
    private final Properties properties;
    private final ObjectMapper objectMapper;
    private final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

    public OpenMeteoService(RestTemplate restTemplate, Properties properties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    private HttpEntity<String> createRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    public CurrentWeatherDTO getForecast(String cityName) {
        try {
            JsonNode geoCoding = getGeoCoding(cityName);

            if (!ObjectUtils.isEmpty(geoCoding)) {
                CurrentWeatherDTO current = getCurrentWeather(
                        geoCoding.get("results").get(0).get("latitude").asText(),
                        geoCoding.get("results").get(0).get("longitude").asText());

                AirQualityDTO airQuality = getAirQuality(
                        geoCoding.get("results").get(0).get("latitude").asText(),
                        geoCoding.get("results").get(0).get("longitude").asText());

                Map<LocalDateTime, Double> carbonMonoxide = new HashMap<>();

                Iterator<LocalDateTime> ik = airQuality.getHourly().getTime().iterator();
                Iterator<Double> ivCarbon = airQuality.getHourly().getCarbon_monoxide().iterator();

                while (ik.hasNext() && ivCarbon.hasNext()) {
                    carbonMonoxide.put(ik.next(), ivCarbon.next());
                }

                Map<LocalDateTime, Double> sulphurDioxide = new HashMap<>();
                Iterator<Double> ivSulphur = airQuality.getHourly().getSulphur_dioxide().iterator();
                while (ik.hasNext() && ivSulphur.hasNext()) {
                    sulphurDioxide.put(ik.next(), ivSulphur.next());
                }

                if (!ObjectUtils.isEmpty(current)) {
                    current.setCityName(geoCoding.get("results").get(0).get("name").asText());
                    current.setState(geoCoding.get("results").get(0).get("admin1").asText());
                    current.setCountry(geoCoding.get("results").get(0).get("country").asText());
                    current.setCarbon_monoxide(carbonMonoxide.get(now));
                    current.setSuphur_dioxide(sulphurDioxide.get(now));
                    return current;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("=> erro ao consultar Cidade {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private JsonNode getGeoCoding(String cityName) {

        HttpEntity<String> request = createRequest("");
        ResponseEntity<String> response;

        try {
            response = this.restTemplate.exchange(
                    this.properties.getOpenMeteo().getUrlGeocoding()
                            .concat("/search?name=").concat(cityName)
                            .concat("&count=1")
                            .concat("&language=pt")
                            .concat("&format=json"),
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                JsonNode geoCoding = this.objectMapper.readTree(response.getBody());
                return geoCoding;
            }
        } catch (Exception e) {
            log.error("=> erro ao consultar Cidade {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private CurrentWeatherDTO getCurrentWeather(String latitude, String longitude) {

        HttpEntity<String> request = createRequest("");
        ResponseEntity<CurrentWeatherDTO> response;

        try {
            response = this.restTemplate.exchange(
                    this.properties.getOpenMeteo().getUrlForecast()
                            .concat("/forecast?")
                            .concat("latitude=").concat(latitude)
                            .concat("&longitude=").concat(longitude)
                            .concat("&current_weather=true")
                            .concat("&timezone=America/Sao_Paulo")
                            .concat("&past_days=0"),
                    HttpMethod.GET,
                    request,
                    CurrentWeatherDTO.class,
                    1
            );
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("=> erro ao consultar Cidade {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private AirQualityDTO getAirQuality(String latitude, String longitude) {
        HttpEntity<String> request = createRequest("");
        ResponseEntity<AirQualityDTO> response;

        try {
            response = this.restTemplate.exchange(
                    this.properties.getOpenMeteo().getUrlAirQuality()
                            .concat("/air-quality?")
                            .concat("latitude=").concat(latitude)
                            .concat("&longitude=").concat(longitude)
                            .concat("&hourly=carbon_monoxide,sulphur_dioxide")
                            .concat("&timezone=America/Sao_Paulo"),
                    HttpMethod.GET,
                    request,
                    AirQualityDTO.class,
                    1
            );
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("=> erro ao consultar Cidade {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
