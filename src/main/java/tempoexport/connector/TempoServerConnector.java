package tempoexport.connector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import tempoexport.dto.server.account.ServerAccountDto;
import tempoexport.dto.server.account.ServerAccountInsertResponseDto;
import tempoexport.dto.server.account.ServerAccountLinksDto;
import tempoexport.dto.server.account.TempoServerAccountDto;
import tempoexport.dto.server.team.ServerTeamInsertResponseDto;
import tempoexport.dto.server.team.ServerTeamDto;
import tempoexport.dto.server.team.members.ServerTeamMemberDto;
import tempoexport.dto.server.team.members.ServerTeamMemberInsertResponseDto;
import tempoexport.dto.server.user.JiraServerUserResultsDto;
import tempoexport.dto.server.worklog.*;

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

    @Value("${jira.server.max.users}")
    private Integer jiraServerMaxUsers;


    public Integer serverWorklogDeletionErrorCounter403 = 0;
    public Integer serverWorklogDeletionErrorCounter500 = 0;
    public Integer serverWorklogInsertionErrorCounter403 = 0;
    public Integer serverWorklogInsertionErrorCounter400 = 0;
    public Integer serverWorklogInsertionErrorCounter500 = 0;
    public Integer serverWorklogUsersWithoutName = 0;
    public Integer worklogsCreated = 0;

    public TempoServerAccountDto[] getTempoServerAccounts() {
        try {
            ResponseEntity<TempoServerAccountDto[]> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-accounts/1/account",
                    HttpMethod.GET, getEntity(), TempoServerAccountDto[].class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public ServerAccountInsertResponseDto insertTempoServerAccount(ServerAccountDto insertAccount) {
        try {
            ResponseEntity<ServerAccountInsertResponseDto> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-accounts/1/account",
                    HttpMethod.POST, getEntityAccount(insertAccount), ServerAccountInsertResponseDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public void deleteTempoServerAccounts(Integer accountId) {
        try {
            restTemplate.exchange(tempoServerUrl + "/rest/tempo-accounts/1/account/{id}", HttpMethod.DELETE, getEntity(), void.class, accountId);
        } catch (HttpStatusCodeException sce) {
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public ServerAccountLinksDto insertTempoServerAccountLinks(ServerAccountLinksDto insertLinksDto) {
        try {
            ResponseEntity<ServerAccountLinksDto> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-accounts/1/link", HttpMethod.POST, getEntityLinks(insertLinksDto), ServerAccountLinksDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public void deleteTempoServerAccountLinks(Integer linkId) {
        try {
            restTemplate.exchange(tempoServerUrl + "/rest/tempo-accounts/1/link/{linkId}", HttpMethod.DELETE, getEntity(), void.class, linkId);
        } catch (HttpStatusCodeException sce) {
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public ServerTeamDto[] getTempoServerTeams() {
        try {
            ResponseEntity<ServerTeamDto[]> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-teams/2/team",
                    HttpMethod.GET, getEntity(), ServerTeamDto[].class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public ServerTeamInsertResponseDto insertTempoServerTeam(ServerTeamDto insertTeam) {
        try {
            ResponseEntity<ServerTeamInsertResponseDto> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-teams/2/team",
                    HttpMethod.POST, getEntityTeam(insertTeam), ServerTeamInsertResponseDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public ServerTeamMemberInsertResponseDto insertTempoServerTeamMember(ServerTeamMemberDto insertTeamMember, Integer id) {
        try {
            ResponseEntity<ServerTeamMemberInsertResponseDto> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-teams/2/team/{id}/member",
                    HttpMethod.POST, getEntityMember(insertTeamMember), ServerTeamMemberInsertResponseDto.class, id);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            if (sce.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return null;
            }
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public void deleteTempoServerTeams(String teamId) {
        try {
            restTemplate.exchange(tempoServerUrl + "/rest/tempo-teams/2/team/{id}", HttpMethod.DELETE, getEntity(), void.class, teamId);
        } catch (HttpStatusCodeException sce) {
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public ServerAccountLinksDto insertServerTeamLinks(ServerAccountLinksDto insertLinksDto) {
        try {
            ResponseEntity<ServerAccountLinksDto> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-teams/1/link",
                    HttpMethod.POST, getEntityLinks(insertLinksDto), ServerAccountLinksDto.class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public ServerAccountLinksDto[] getTempoServerSingleAccountLinks(Integer accountId) {
        try {
            ResponseEntity<ServerAccountLinksDto[]> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-accounts/1/account/{accountId}/link",
                    HttpMethod.GET, getEntity(), ServerAccountLinksDto[].class, accountId);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public JiraServerUserResultsDto[] getJiraServerUsers() {
        try {
            ResponseEntity<JiraServerUserResultsDto[]> usage = restTemplate.exchange(tempoServerUrl + "/rest/api/2/user/search?username=.&amp;startAt=0&maxResults=" + jiraServerMaxUsers,
                    HttpMethod.GET, getEntity(), JiraServerUserResultsDto[].class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            log.error("Status Code exception {}", sce);
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public boolean insertTempoServerWorklog(ServerWorklogDto insertWorklog) {
        try {
            restTemplate.exchange(tempoServerUrl + "/rest/tempo-timesheets/4/worklogs",
                    HttpMethod.POST, getEntityInsertWorklogs(insertWorklog), void.class);
            worklogsCreated++;
            return true;
        } catch (HttpStatusCodeException sce) {
            if (sce.getStatusCode() == HttpStatus.FORBIDDEN) {
                log.error(sce.getStatusCode().toString());
                log.error(sce.getMessage());
                serverWorklogInsertionErrorCounter403++;
                log.info("Total 403 errors: {}", serverWorklogInsertionErrorCounter403);
            }
            if (sce.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.error(sce.getStatusCode().toString());
                log.error(sce.getMessage());
                serverWorklogInsertionErrorCounter400++;
                log.info("Total 400 errors: {}", serverWorklogInsertionErrorCounter400);
            }
            if (sce.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.error(sce.getStatusCode().toString());
                log.error(sce.getMessage());
                serverWorklogInsertionErrorCounter500++;
                log.info("Total 500 errors: {}", serverWorklogInsertionErrorCounter500);
            }
            return false;
        }
    }

    public TempoServerReturnWorklogDto[] getTempoServerWorklogs(TempoServerWorklogRequestDto tempoServerWorklogRequestDto) {
        try {
            ResponseEntity<TempoServerReturnWorklogDto[]> usage = restTemplate.exchange(tempoServerUrl + "/rest/tempo-timesheets/4/worklogs/search",
                    HttpMethod.POST, getEntityWorklog(tempoServerWorklogRequestDto), TempoServerReturnWorklogDto[].class);
            return usage.getBody();
        } catch (HttpStatusCodeException sce) {
            throw new RuntimeException("Status code exception ", sce);
        }
    }

    public boolean deleteTempoServerWorklog(Integer worklogId) {
        try {
            restTemplate.exchange(tempoServerUrl + "/rest/tempo-timesheets/4/worklogs/{worklogId}",
                    HttpMethod.DELETE, getEntity(), void.class, worklogId);
            return true;
        } catch (HttpStatusCodeException sce) {
            if (sce.getStatusCode() == HttpStatus.FORBIDDEN) {
                log.error(sce.getStatusCode().toString());
                log.error(sce.getMessage());
                serverWorklogDeletionErrorCounter403++;
                log.info("Total 403 errors: {}", serverWorklogDeletionErrorCounter403);
            }
            if (sce.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.error(sce.getStatusCode().toString());
                log.error(sce.getMessage());
                serverWorklogDeletionErrorCounter500++;
                log.info("Total 500 errors: {}", serverWorklogDeletionErrorCounter500);
            }
            return false;
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(usernameServer, passwordServer);
        return headers;
    }

    private HttpEntity getEntity() {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(null, headers);
        return httpEntity;
    }

    private HttpEntity getEntityAccount(ServerAccountDto account) {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(account, headers);
        return httpEntity;
    }

    private HttpEntity getEntityTeam(ServerTeamDto team) {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(team, headers);
        return httpEntity;
    }

    private HttpEntity getEntityMember(ServerTeamMemberDto member) {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(member, headers);
        return httpEntity;
    }

    private HttpEntity getEntityLinks(ServerAccountLinksDto insertLinksDto) {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(insertLinksDto, headers);
        return httpEntity;
    }

    private HttpEntity getEntityWorklog(TempoServerWorklogRequestDto worklog) {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(worklog, headers);
        return httpEntity;
    }

    private HttpEntity getEntityInsertWorklogs(ServerWorklogDto worklog) {
        HttpHeaders headers = getHeaders();
        HttpEntity httpEntity = new HttpEntity(worklog, headers);
        return httpEntity;
    }
}