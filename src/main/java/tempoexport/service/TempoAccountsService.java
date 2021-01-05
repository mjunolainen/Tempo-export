package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.account.CloudAccountLinksDto;
import tempoexport.dto.cloud.account.CloudAccountLinksScopeDto;
import tempoexport.dto.cloud.account.CloudAccountResultsDto;
import tempoexport.dto.cloud.account.TempoCloudAccountDto;
import tempoexport.dto.cloud.worklog.WorkLogDto;
import tempoexport.dto.server.account.ServerAccountDto;
import tempoexport.dto.server.account.ServerAccountInsertResponseDto;
import tempoexport.dto.server.account.ServerAccountLinksDto;
import tempoexport.dto.server.account.TempoServerAccountDto;
import tempoexport.dto.server.user.JiraServerUserDto;
import tempoexport.dto.server.user.JiraServerUserResultsDto;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class TempoAccountsService {
    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;

    public void migrateTempoAccounts() {
        TempoCloudAccountDto dto = tempoCloudConnector.getTempoCloudAccounts();

        if (dto.getResults() != null) {
            for (CloudAccountResultsDto cloudAccountResultsDto : dto.getResults()) {
                ServerAccountDto insertDto = new ServerAccountDto();
                BeanUtils.copyProperties(cloudAccountResultsDto, insertDto);

                String cloudDisplayName = cloudAccountResultsDto.getCloudAccountResultsLeadDto().getDisplayName();
                String serverUserKey = jiraServerUserKey(cloudDisplayName);

                JiraServerUserDto serverAccountLeadDto = new JiraServerUserDto();
                serverAccountLeadDto.setUsername(cloudDisplayName);
                serverAccountLeadDto.setKey(serverUserKey);
                insertDto.setJiraServerLead(serverAccountLeadDto);

                JiraServerUserDto serverAccountContactDto = new JiraServerUserDto();
                serverAccountContactDto.setUsername("");
                serverAccountContactDto.setKey("");
                insertDto.setJiraServerContact(serverAccountContactDto);

                ServerAccountInsertResponseDto tempoServerAccount = tempoServerConnector.insertAccount(insertDto);
                log.info("Account created: {}", insertDto.getKey());

                String tempoCloudLinksApi = cloudAccountResultsDto.getCloudAccountResultsLinksDto().getSelf();
                log.info("tempoServerAccount {}", tempoCloudLinksApi);

                CloudAccountLinksDto tempoCloudLinksDto = tempoCloudConnector.getTempoCloudAccountLinks(tempoCloudLinksApi);
                log.info("tempoCloudLinksDto {}", tempoCloudLinksDto.toString());
                /*if (tempoCloudLinksDto.getResults() != null) {
                    for (CloudAccountLinksScopeDto cloudAccountLinksScopeDto : tempoCloudLinksDto.getResults()) {
                        ServerAccountLinksDto insertLinksDto = new ServerAccountLinksDto();
                        insertLinksDto.setAccountId(tempoServerAccount.getId());
                        insertLinksDto.setKey(tempoServerAccount.getKey());
                        insertLinksDto.setLinkType("MANUAL");
                        insertLinksDto.setScope(cloudAccountLinksScopeDto.getId());
                        insertLinksDto.setScopeType(cloudAccountLinksScopeDto.getType());
                        tempoServerConnector.insertLinks(insertLinksDto);
                        log.info("Links inserted for account: {}", insertDto.getKey());
                    }
                }*/
            }
        }
    }

    public String jiraServerUserKey(String cloudDisplayName) {
        String serverUserKey = null;
        if (jiraServerUserMap().containsKey(cloudDisplayName)) {
            serverUserKey = jiraServerUserMap().get(cloudDisplayName).getKey();
        }
        return serverUserKey;
    }

    public Map<String, JiraServerUserResultsDto> jiraServerUserMap() {
        JiraServerUserResultsDto[] dto = tempoServerConnector.getJiraServerUsers();
        Map<String, JiraServerUserResultsDto> paramMap = new HashMap<>();

        if (dto.length > 0) {
            for (JiraServerUserResultsDto userKeyDto : dto) {
                paramMap.put(userKeyDto.getDisplayName(), userKeyDto);
            }
        }
        return paramMap;
    }

    public void tempoServerAccounts() {
        TempoServerAccountDto[] dto = tempoServerConnector.getTempoServerAccounts();
        log.info(String.valueOf(dto.length));
        for (int i = 0; i < dto.length; i++) {
            TempoServerAccountDto tempoServerAccountIdDto = dto[i];
            tempoServerAccountIdDto.getId();
            tempoServerAccountIdDto.getKey();
            log.info(tempoServerAccountIdDto.getId().toString(), tempoServerAccountIdDto.getKey());
        }
    }

    public void deleteTempoServerAccounts() {
        TempoServerAccountDto[] dto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < dto.length; i++) {
            TempoServerAccountDto dtoAccount = dto[i];
            tempoServerConnector.deleteTempoServerAccounts(dtoAccount.getId());
            log.info("Account deleted for: {}", dtoAccount.getKey());
        }
    }

    public Map<String, Integer> tempoServerAccountKeyIdMap(){
        TempoServerAccountDto[] dto = tempoServerConnector.getTempoServerAccounts();
        Map<String, Integer> paramMap = new HashMap<>();

        if (dto.length > 0){
            for (TempoServerAccountDto userKeyDto : dto){
                paramMap.put(userKeyDto.getKey(), userKeyDto.getId());
            }
        }
        return paramMap;
    }

    public void getTempoCloudAccounts() {
        TempoCloudAccountDto dto = tempoCloudConnector.getTempoCloudAccounts();
        log.info(String.valueOf(dto));
    }

    public void tempoData() {
        WorkLogDto dto = tempoCloudConnector.getWorklogs();
        log.info("Count {}", dto.getMetaData().getCount());
        log.info("Offset {}", dto.getMetaData().getOffset());
        log.info("Limit {}", dto.getMetaData().getLimit());
    }
}