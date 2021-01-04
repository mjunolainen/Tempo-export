package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.dto.*;
import tempoexport.connector.TempoServerConnector;

import java.util.Arrays;
import java.util.HashMap;
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
        //log.info("Count {}", dto.getMetaData().getCount());
        //log.info("Results {}", dto.getResults());

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

                //log.info("insertDto contact {}", insertDto.getJiraServerContact());
                //log.info("insertDto key {}", insertDto.getKey());
                //log.info("insertDto serverLead {}", insertDto.getJiraServerLead());
                //log.info("insertDto name {}", insertDto.getName());
                //log.info("insertDto status {}", insertDto.getStatus());

                ServerAccountInsertResponseDto responseDto = tempoServerConnector.insertAccount(insertDto);
                log.info("response object for post {}", responseDto.toString());
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
    }

    public void deleteTempoServerAccounts() {
        TempoServerAccountDto[] dto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < dto.length; i++) {
            TempoServerAccountDto dtoAccount = dto[i];
            Integer accountId = dtoAccount.getId();
            tempoServerConnector.deleteTempoServerAccounts(accountId);
        }
    }

    public void tempoData() {
        WorkLogDto dto = tempoCloudConnector.getWorklogs();
        log.info("Count {}", dto.getMetaData().getCount());
        log.info("Offset {}", dto.getMetaData().getOffset());
        log.info("Limit {}", dto.getMetaData().getLimit());
    }
}