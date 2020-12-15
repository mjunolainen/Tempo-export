package Tempoexport.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import Tempoexport.repository.TempoExportRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class TempoExportService {

    @Autowired
    private TempoExportRepository tempoExportRepository;
    private RestTemplate restTemplate;

    @Value("fcg42Im2JmExadbA31ZaWHTFzlK5Ll")
    private String bearer;

    @Value("https://api.tempo.io/core/3/worklogs")
    private String address;

    /*curl --location --request GET “https://api.tempo.io/core/3/worklogs” \
    --header “Authorization: Bearer fcg42Im2JmExadbA31ZaWHTFzlK5Ll” \*/

    public void tempoData() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(address)
                .queryParam("bearer", bearer);
        Map response = restTemplate.getForObject(url.toUriString(), HashMap.class);

    }
}
