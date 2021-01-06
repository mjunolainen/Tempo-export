package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.account.*;
import tempoexport.dto.server.account.ServerAccountDto;
import tempoexport.dto.server.account.ServerAccountInsertResponseDto;
import tempoexport.dto.server.account.ServerAccountLinksDto;
import tempoexport.dto.server.account.TempoServerAccountDto;
import tempoexport.dto.server.user.JiraServerUserDto;
import tempoexport.dto.server.user.JiraServerUserResultsDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TempoAccountsService {
    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;

    Map<String, JiraServerUserResultsDto> jiraUserServerMap = null;

    public void migrateTempoAccounts() {

        // Serveri accoundi linkide kustutamine
        TempoServerAccountDto[] getTempoAccountsDto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < getTempoAccountsDto.length; i++) {
            TempoServerAccountDto tempoServerAccountDto = getTempoAccountsDto[i];
            ServerAccountLinksDto[] tempoServerSingleAccountLinks = tempoServerConnector.getTempoServerSingleAccountLinks(tempoServerAccountDto.getId());
            for (int j = 0; j < tempoServerSingleAccountLinks.length; j++) {
                ServerAccountLinksDto tempoServerSingleAccountLink = tempoServerSingleAccountLinks[j];
                log.info(tempoServerSingleAccountLink.toString());
                tempoServerConnector.deleteTempoServerAccountLinks(tempoServerSingleAccountLink.getId());
                log.info("Link {} deleted for account {}", tempoServerSingleAccountLink.getName(), tempoServerAccountDto.getKey());
            }
        }

        // Serveri accountide kustutamine
        TempoServerAccountDto[] deleteTempoServerAccountDto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < deleteTempoServerAccountDto.length; i++) {
            TempoServerAccountDto dtoAccount = deleteTempoServerAccountDto[i];
            tempoServerConnector.deleteTempoServerAccounts(dtoAccount.getId());
            log.info("Account deleted for: {}", dtoAccount.getKey());
        }

        // Serveri accountide migreerimine pilvest serverisse
        TempoCloudAccountDto dto = tempoCloudConnector.getTempoCloudAccounts();
        if (dto.getResults() != null) {
            for (CloudAccountResultsDto cloudAccountResultsDto : dto.getResults()) {
                ServerAccountDto insertDto = new ServerAccountDto();
                BeanUtils.copyProperties(cloudAccountResultsDto, insertDto);

                // Account lead pilvest serverisse.
                String cloudLeadDisplayName = cloudAccountResultsDto.getCloudAccountResultsLeadDto().getDisplayName();
                String serverLeadUserKey = jiraServerUserKey(cloudLeadDisplayName);
                JiraServerUserDto serverAccountLeadDto = new JiraServerUserDto();
                serverAccountLeadDto.setUsername(cloudLeadDisplayName);
                serverAccountLeadDto.setKey(serverLeadUserKey);
                insertDto.setJiraServerLead(serverAccountLeadDto);
                // TODO set customer. Sama moodi nagu account lead ja contact

                // Account contact pilvest serverisse.
                JiraServerUserDto serverAccountContactDto = new JiraServerUserDto();
                serverAccountContactDto.setUsername("");
                serverAccountContactDto.setKey("");
                insertDto.setJiraServerContact(serverAccountContactDto);

                /*String cloudContactDisplayName = cloudAccountResultsDto.getCloudAccountResultsContactDto().getDisplayName();
                String serverContactUserKey;
                if (cloudContactDisplayName != null) {
                    serverContactUserKey = jiraServerUserKey(cloudContactDisplayName);
                } else {
                    cloudContactDisplayName = "";
                    serverContactUserKey = "";
                }
                JiraServerUserDto serverAccountContactDto = new JiraServerUserDto();
                serverAccountContactDto.setUsername(cloudContactDisplayName);
                serverAccountContactDto.setKey(serverContactUserKey);
                insertDto.setJiraServerContact(serverAccountContactDto);*/

                ServerAccountInsertResponseDto tempoServerAccount = tempoServerConnector.insertAccount(insertDto);
                log.info("Account created: {}", insertDto.getKey());

                String tempoCloudLinksApi = cloudAccountResultsDto.getCloudAccountResultsLinksDto().getSelf();
                log.info("tempoServerAccount {}", tempoCloudLinksApi);

                CloudAccountLinksDto tempoCloudLinksDto = tempoCloudConnector.getTempoCloudAccountLinks(tempoCloudLinksApi);
                log.info("tempoCloudLinksDto {}", tempoCloudLinksDto.toString());

                if (tempoCloudLinksDto.getResults() != null) {
                    for (CloudAccountLinksResultsDto cloudAccountLinksResultsDto : tempoCloudLinksDto.getResults()) {
                        ServerAccountLinksDto insertLinksDto = new ServerAccountLinksDto();
                        insertLinksDto.setAccountId(tempoServerAccount.getId());
                        insertLinksDto.setKey(tempoServerAccount.getKey());
                        insertLinksDto.setLinkType("MANUAL");
                        insertLinksDto.setScope(cloudAccountLinksResultsDto.getCloudAccountLinksScopeDto().getId());
                        insertLinksDto.setScopeType(cloudAccountLinksResultsDto.getCloudAccountLinksScopeDto().getType());
                        tempoServerConnector.insertLinks(insertLinksDto);
                        log.info("Links inserted for account: {}", insertDto.getKey());
                    }
                }
            }
        }
    }

    private String jiraServerUserKey(String cloudDisplayName) {
        String serverUserKey = null;
        if (jiraServerUserMap().containsKey(cloudDisplayName)) {
            serverUserKey = jiraServerUserMap().get(cloudDisplayName).getKey();
        }
        return serverUserKey;
    }

    private Map<String, JiraServerUserResultsDto> jiraServerUserMap() {
        if (jiraUserServerMap == null) {
            JiraServerUserResultsDto[] dto = tempoServerConnector.getJiraServerUsers();
            Map<String, JiraServerUserResultsDto> paramMap = new HashMap<>();

            if (dto.length > 0) {
                for (JiraServerUserResultsDto userKeyDto : dto) {
                    paramMap.put(userKeyDto.getDisplayName(), userKeyDto);
                }
            }
            jiraUserServerMap = paramMap;
            return jiraUserServerMap;
        } else {
            return jiraUserServerMap;
        }
    }
}