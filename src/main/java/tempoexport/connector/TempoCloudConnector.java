package tempoexport.connector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import tempoexport.dto.cloud.account.CloudLinksDto;
import tempoexport.dto.cloud.account.TempoCloudAccountDto;
import tempoexport.dto.cloud.team.CloudTeamsListDto;
import tempoexport.dto.cloud.team.members.CloudTeamMembersDto;
import tempoexport.dto.cloud.worklog.WorkLogDto;

@Slf4j
@Component
public class TempoCloudConnector {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tempo.cloud.url}")
    private String tempoCloudUrl;

    @Value("${tempo.token}")
    private String token;


    public WorkLogDto getWorklogs() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<WorkLogDto> usage = restTemplate.exchange(tempoCloudUrl + "/worklogs", HttpMethod.GET, httpEntity, WorkLogDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public TempoCloudAccountDto getTempoCloudAccounts() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<TempoCloudAccountDto> usage = restTemplate.exchange(tempoCloudUrl + "/accounts", HttpMethod.GET, httpEntity, TempoCloudAccountDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public CloudTeamsListDto getTempoCloudTeams() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<CloudTeamsListDto> usage = restTemplate.exchange(tempoCloudUrl + "/teams", HttpMethod.GET, httpEntity, CloudTeamsListDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public CloudLinksDto getTempoCloudAccountLinks(String tempoCloudLinksApi) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<CloudLinksDto> usage = restTemplate.exchange(tempoCloudLinksApi, HttpMethod.GET, httpEntity, CloudLinksDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public CloudTeamMembersDto getTempoCloudTeamMembers(String tempoCloudTeamMembersApi) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<CloudTeamMembersDto> usage = restTemplate.exchange(tempoCloudTeamMembersApi, HttpMethod.GET, httpEntity, CloudTeamMembersDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }
}