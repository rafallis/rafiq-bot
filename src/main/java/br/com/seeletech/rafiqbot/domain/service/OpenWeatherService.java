package br.com.seeletech.rafiqbot.domain.service;

import br.com.seeletech.rafiqbot.domain.OpenWeather;
import br.com.seeletech.rafiqbot.domain.OpenWeatherDTO;
import br.com.seeletech.rafiqbot.domain.OpenWeatherMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenWeatherService {

    @Value("${app.openweather.url-icon}")
    private String openweatherIconUrl;

    @Value("${app.openweather.url-base}")
    private String openWeatherBaseUrl;

    @Value("${app.openweather.token}")
    private String openWeatherToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenWeatherService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private HttpEntity<String> createRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    public String buildIconUrl(String iconCode) {
        return openweatherIconUrl + "/" +
                iconCode +
                "@2x.png";
    }

    public OpenWeatherDTO getWeather(String city) throws Exception {
        HttpEntity<String> request = createRequest("");
        ResponseEntity<String> response;
        String url = openWeatherBaseUrl + "?q=" + city +
                "&appid=" + openWeatherToken +
                "&units=metric";

        try {
            response = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                OpenWeather weather = this.objectMapper.readValue(response.getBody(), OpenWeather.class);
                return OpenWeatherMapper.INSTANCE.convert(weather);
            }
        } catch (JsonProcessingException jpe) {
            throw new Exception("erro ao processar retorno da api");
        }
        return null;
    }
}
