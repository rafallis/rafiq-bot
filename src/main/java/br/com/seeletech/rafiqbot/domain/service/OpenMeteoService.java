package br.com.seeletech.rafiqbot.domain.service;

import br.com.seeletech.rafiqbot.domain.AirQuality;
import br.com.seeletech.rafiqbot.infrastructure.configuration.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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

    public OpenMeteoService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    private HttpEntity<String> createRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    public Map<String, String> getAirQuality(String latitude, String longitude) throws Exception {
        HttpEntity<String> request = createRequest("");
        ResponseEntity<AirQuality> response;

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
                    AirQuality.class,
                    1
            );
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                AirQuality airQuality = response.getBody();

                Map<LocalDateTime, Double> carbonMonoxide = new HashMap<>();
                Map<LocalDateTime, Double> sulphurDioxide = new HashMap<>();

                Iterator<LocalDateTime> ik = airQuality.getHourly().getTime().iterator();
                Iterator<Double> ivCarbon = airQuality.getHourly().getCarbon_monoxide().iterator();
                Iterator<Double> ivSulphur = airQuality.getHourly().getSulphur_dioxide().iterator();
                while (ik.hasNext() && ivCarbon.hasNext()) {
                    carbonMonoxide.put(ik.next(), ivCarbon.next());
                    sulphurDioxide.put(ik.next(), ivSulphur.next());
                }

                LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
                Map<String, String> airQualityMap = Map.of(
                        "co", String.valueOf(carbonMonoxide.get(now)),
                        "so2", String.valueOf(sulphurDioxide.get(now))
                );

                return airQualityMap;
            }
        } catch (NullPointerException npe) {
            log.debug("=> error filling AirQualityDTO object {}", npe.getMessage());
            throw new NullPointerException("error filling AirQualityDTO object");
        } catch (Exception e) {
            log.debug("=> erro ao consultar Cidade {}", e.getMessage());
            throw new Exception("error when trying to get city air quality");
        }
        return null;
    }
}
