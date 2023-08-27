package br.com.seeletech.rafiqbot.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RandomCatService {

    @Value("${app.thecatapi.url-base}")
    private String thecatBaseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RandomCatService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private HttpEntity<String> createRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    public String getRandomCatUrl() throws Exception {
        HttpEntity<String> request = createRequest("");
        ResponseEntity<String> response;

        try {
            response = this.restTemplate.exchange(
                    thecatBaseUrl,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                JsonNode thecat = this.objectMapper.readTree(response.getBody());
                return thecat.get(0).get("url").asText();
            }
        } catch (Exception jpe) {
            throw new Exception("erro ao processar retorno de TheCatApi");
        }
        return null;
    }
}
