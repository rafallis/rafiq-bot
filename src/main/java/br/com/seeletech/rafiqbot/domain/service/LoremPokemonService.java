package br.com.seeletech.rafiqbot.domain.service;

import br.com.seeletech.rafiqbot.configuration.Properties;
import br.com.seeletech.rafiqbot.dto.CurrentWeatherDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
public class LoremPokemonService {

    private final RestTemplate restTemplate;
    private final Properties properties;
    private final ObjectMapper objectMapper;

    public LoremPokemonService(RestTemplate restTemplate, Properties properties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    private HttpEntity<String> createRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    public String getRandomPokemonUrl() {
        HttpEntity<String> request = createRequest("");
        ResponseEntity<String> response;

        try {
            response = this.restTemplate.exchange(
                    this.properties.getLoremPokemon().getUrl(),
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            if (response.getStatusCode().equals(HttpStatus.FOUND)) {
                HttpHeaders headers = response.getHeaders();
                URI location = headers.getLocation();
                log.info("=> pokemon:{}", location.toString().substring(location.toString().length() - 3));
                return location.toString();
            }
        } catch (Exception e) {
            log.error("=> erro ao consultar Cidade {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
