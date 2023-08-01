package br.com.seeletech.rafiqbot.service;

import br.com.seeletech.rafiqbot.configuration.Properties;
import br.com.seeletech.rafiqbot.dto.CurrentWeatherDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OpenMeteoService {

    private final RestTemplate restTemplate;
    private final Properties properties;
    private final ObjectMapper objectMapper;

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

        HttpEntity<String> request = createRequest("");
        ResponseEntity<CurrentWeatherDTO> response;

        try {
            JsonNode geoCoding = getGeoCoding(cityName);
            response = this.restTemplate.exchange(
                    this.properties.getOpenMeteo().getUrlForecast()
                            .concat("/forecast?")
                            .concat("latitude=").concat(geoCoding.get("results").get(0).get("latitude").asText())
                            .concat("&longitude=").concat(geoCoding.get("results").get(0).get("longitude").asText())
                            .concat("&current_weather=true")
                            .concat("&timezone=America/Sao_Paulo"),
                    HttpMethod.GET,
                    request,
                    CurrentWeatherDTO.class,
                    1
            );
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                CurrentWeatherDTO current = response.getBody();
                current.setCityName(geoCoding.get("results").get(0).get("name").asText());
                current.setState(geoCoding.get("results").get(0).get("admin1").asText());
                current.setCountry(geoCoding.get("results").get(0).get("country").asText());
                return current;
            }
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
}
