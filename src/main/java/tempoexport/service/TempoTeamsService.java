package tempoexport.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.account.CloudAccountResultsDto;
import tempoexport.dto.cloud.account.CloudLinksDto;
import tempoexport.dto.cloud.account.CloudLinksResultsDto;
import tempoexport.dto.cloud.team.CloudTeamResultsDto;
import tempoexport.dto.cloud.team.TempoCloudTeamDto;
import tempoexport.dto.server.account.ServerAccountLinksDto;
import tempoexport.dto.server.team.ServerTeamInsertResponseDto;
import tempoexport.dto.server.team.ServerTeamResultsDto;
import tempoexport.dto.server.user.JiraServerUserResultsDto;
import tempoexport.dto.server.user.ServerTempoUserDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TempoTeamsService {

    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;

    Map<String, JiraServerUserResultsDto> getJiraUserServerMap = null;

    public void migrateTempoTeams() {

        // Serveri teamide kustutamine
        ServerTeamResultsDto[] deleteTempoServerTeamDto = tempoServerConnector.getTempoServerTeams();
        for (int i = 0; i < deleteTempoServerTeamDto.length; i++) {
            ServerTeamResultsDto dtoTeam = deleteTempoServerTeamDto[i];
            tempoServerConnector.deleteTempoServerTeams(dtoTeam.getId());
            log.info("Team {} deleted", dtoTeam.getName());
        }

        // Teami migratsiooni pilvest serverisse
        TempoCloudTeamDto dto = tempoCloudConnector.getTempoCloudTeams();
        if (dto.getResults() != null) {
            for (CloudTeamResultsDto cloudTeamResultsDto : dto.getResults()) {
                ServerTeamResultsDto insertDto = new ServerTeamResultsDto();

                insertDto.setName(cloudTeamResultsDto.getName());
                insertDto.setSummary(cloudTeamResultsDto.getSummary());

                ServerTempoUserDto serverTempoTeamLeadDto = new ServerTempoUserDto();
                serverTempoTeamLeadDto.setDisplayname(cloudTeamResultsDto.getCloudTeamResultsLeadDto().getDisplayName());
                String serverLeadUserKey = getJiraServerUserKey(serverTempoTeamLeadDto.getDisplayname());
                serverTempoTeamLeadDto.setKey(serverLeadUserKey);
                serverTempoTeamLeadDto.setJiraUser(true);
                serverTempoTeamLeadDto.setName(getJiraUserName(serverLeadUserKey));
                insertDto.setServerTempoLeadUserDto(serverTempoTeamLeadDto);
                insertDto.setLead(serverLeadUserKey);

                //TODO Team --> program migratsioon on vaja siit välja tõsta. Sellisel kujul see ei tööta.
               /* if (cloudTeamResultsDto != null && cloudTeamResultsDto.getCloudTeamResultsProgramDto() != null) {
                    ServerTeamResultsTeamProgramDto serverTempoTeamProgramDto = new ServerTeamResultsTeamProgramDto();
                    serverTempoTeamProgramDto.setId((cloudTeamResultsDto.getCloudTeamResultsProgramDto().getId()));
                    serverTempoTeamProgramDto.setName(cloudTeamResultsDto.getCloudTeamResultsProgramDto().getName());
                    insertDto.setServerTeamResultsTeamProgramDto(serverTempoTeamProgramDto);
                }*/
                ServerTeamInsertResponseDto tempoServerTeam = tempoServerConnector.insertTeam(insertDto);
                log.info("Team {} created.", insertDto.getName());
                /*

                String tempoTeamCloudLinksApi = dto.getSelf();
                CloudLinksDto tempoTeamsCloudLinksDto = tempoCloudConnector.getTempoCloudAccountLinks(tempoTeamCloudLinksApi);
                if (tempoTeamsCloudLinksDto.getResults() != null) {
                    for (CloudLinksResultsDto cloudLinksResultsDto : tempoTeamsCloudLinksDto.getResults()) {
                        ServerAccountLinksDto insertTeamLinksDto = new ServerAccountLinksDto();
                        insertTeamLinksDto.setAccountId(tempoServerTeam.getId());
                        insertTeamLinksDto.setKey((tempoServerTeam.getKey()));
                        insertTeamLinksDto.setLinkType("MANUAL");
                        insertTeamLinksDto.setScope(cloudLinksResultsDto.getCloudLinksScopeDto().getId());
                        insertTeamLinksDto.setScopeType(cloudLinksResultsDto.getCloudLinksScopeDto().getType());

                        tempoServerConnector.insertTeamLinks(insertTeamLinksDto);
                        log.info("Link to Project inserted: {} to {}", insertTeamLinksDto.getScope(), insertDto.getName());
                    }
                }*/
            }
        }
    }

    private String getJiraServerUserKey(String cloudDisplayName) {
        String serverUserKey = null;
        if (getJiraServerUserMap().containsKey(cloudDisplayName)) {
            serverUserKey = getJiraServerUserMap().get(cloudDisplayName).getKey();
        }
        return serverUserKey;
    }

    private String getJiraUserName(String cloudDisplayName) {
        String serverUserName = null;
        if (getJiraServerUserMap().containsKey(cloudDisplayName)) {
            serverUserName = getJiraServerUserMap().get(cloudDisplayName).getName();
        }
        return serverUserName;
    }

    private Map<String, JiraServerUserResultsDto> getJiraServerUserMap() {
        if (getJiraUserServerMap == null) {
            JiraServerUserResultsDto[] dto = tempoServerConnector.getJiraServerUsers();
            Map<String, JiraServerUserResultsDto> paramMap = new HashMap<>();

            if (dto.length > 0) {
                for (JiraServerUserResultsDto userKeyDto : dto) {
                    paramMap.put(userKeyDto.getDisplayName(), userKeyDto);
                }
            }
            getJiraUserServerMap = paramMap;
            return getJiraUserServerMap;
        } else {
            return getJiraUserServerMap;
        }
    }

    // Eraldi meetodid testimiseks. Hiljem kokku migrateTempoTeams() alla ja võib ära kustutada
    public void tempoCloudTeams() {
        TempoCloudTeamDto dto = tempoCloudConnector.getTempoCloudTeams();
        log.info(dto.toString());
    }

    public void tempoServerTeams() {
        ServerTeamResultsDto[] dto = tempoServerConnector.getTempoServerTeams();
        log.info(String.valueOf(dto.length));
    }

    public void deleteTempoTeams() {
        ServerTeamResultsDto[] deleteTempoServerTeamDto = tempoServerConnector.getTempoServerTeams();
        for (int i = 0; i < deleteTempoServerTeamDto.length; i++) {
            ServerTeamResultsDto dtoTeam = deleteTempoServerTeamDto[i];
            tempoServerConnector.deleteTempoServerTeams(dtoTeam.getId());
            log.info("Team {} deleted", dtoTeam.getName());
        }
    }
}