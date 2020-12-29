package tempoexport.connector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import tempoexport.dto.TempoServerTeamDto;

@Slf4j
@Component
public class TempoServerConnector {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tempo.server.url}")
    private String tempoServerUrl;

    @Value("${tempo.usernameServer}")
    private String usernameServer;

    @Value("${tempo.passwordServer}")
    private String passwordServer;

    public TempoServerTeamDto[] getTempoServerTeams() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(usernameServer, passwordServer);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<TempoServerTeamDto[]> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-teams/2/team", HttpMethod.GET, httpEntity, TempoServerTeamDto[].class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }


}
