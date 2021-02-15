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
import tempoexport.dto.cloud.worklog.CloudWorklogsListDto;

@Slf4j
@Component
public class TempoCloudConnector {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tempo.cloud.url}")
    private String tempoCloudUrl;

    @Value("${tempo.token}")
    private String token;

    @Value("${jira.cloud.get.worklogs.count}")
    private Integer cloudWorklogsCount;


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

    public CloudWorklogsListDto getTempoCloudWorklogs() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<CloudWorklogsListDto> usage = restTemplate.exchange(tempoCloudUrl + "/worklogs/?offset=0&limit=" + cloudWorklogsCount, HttpMethod.GET, httpEntity, CloudWorklogsListDto.class);
            //ResponseEntity<CloudWorklogsListDto> usage = restTemplate.exchange(tempoCloudUrl + "/worklogs/?offset=0&from=2020-12-01&to=2020-12-31&limit=1000", HttpMethod.GET, httpEntity, CloudWorklogsListDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public CloudWorklogsListDto getNextTempoCloudWorklogs(String nextCloudWorklogUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<CloudWorklogsListDto> usage = restTemplate.exchange(nextCloudWorklogUrl, HttpMethod.GET, httpEntity, CloudWorklogsListDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }
}