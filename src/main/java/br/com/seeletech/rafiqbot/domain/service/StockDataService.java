package br.com.seeletech.rafiqbot.domain.service;

import br.com.seeletech.rafiqbot.domain.StockData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockDataService {

    @Value("${app.stock-data.url-base}")
    private String stockDataBaseUrl;

    @Value("${app.stock-data.token}")
    private String apiToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StockDataService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private HttpEntity<String> createRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    public StockData getFinanceData(String symbols) throws Exception {
        HttpEntity<String> request = createRequest("");
        ResponseEntity<String> response;
        String url = stockDataBaseUrl + "?symbols=" + symbols +
                "&api_token=" + apiToken;

        try {
            response = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                StockData stockData = this.objectMapper.readValue(response.getBody(), StockData.class);
                return stockData;
            }
        } catch (JsonProcessingException jpe) {
            throw new Exception("erro ao processar retorno de StockData");
        }
        return null;
    }
}
