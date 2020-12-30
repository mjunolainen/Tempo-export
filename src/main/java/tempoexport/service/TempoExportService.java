package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.dto.*;
import tempoexport.connector.TempoServerConnector;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TempoExportService {

    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;

    public void tempoData() {
        WorkLogDto dto = tempoCloudConnector.getWorklogs();
        log.info("Count {}", dto.getMetaData().getCount());
        log.info("Offset {}", dto.getMetaData().getOffset());
        log.info("Limit {}", dto.getMetaData().getLimit());
    }

    public void tempoCloudAccounts() {
        TempoCloudAccountDto dto = tempoCloudConnector.getTempoCloudAccounts();
        log.info("Count {}", dto.getMetaData().getCount());
        log.info("Results {}", dto.getResults());

        if (dto.getResults() != null) {
            for (CloudAccountResultsDto cloudAccountResultsDto : dto.getResults()) {
                ServerAccountDto insertDto = new ServerAccountDto();

                BeanUtils.copyProperties(cloudAccountResultsDto, insertDto);

                //.copyProperties võtab ainult ühilduvad kaasa? key-key, name-name ja status-status
                //TODO insertDto: contact --> tühi {key: {}, username:{}}, JiraServerUserDto
                //TODO insertDto: lead --> JiraServerUserDto
                // cloud displayName = server username,
                // get displayName --> jiraServerUserKey() --> leia displayName vastav key

                log.info("insertDto key {}", insertDto.getKey());
                ServerAccountInsertResponseDto responseDto = tempoServerConnector.insertAccount(insertDto);
                log.info("response object for post {}", responseDto.toString());
            }
        }
    }

    public void tempoCloudTeams() {
        TempoCloudTeamDto dto = tempoCloudConnector.getTempoCloudTeams();
        log.info(String.valueOf(dto.getResults()));
    }

    public void tempoServerTeams() {
        TempoServerTeamDto[] dto = tempoServerConnector.getTempoServerTeams();
        log.info(String.valueOf(dto.length));
    }

    public void tempoServerAccounts() {
        TempoServerAccountDto[] dto = tempoServerConnector.getTempoServerAccounts();
        log.info(String.valueOf(dto.length));
    }

    public void jiraServerUsers() {
        JiraServerUserResultsDto[] dto = tempoServerConnector.getJiraServerUsers();
        Map<String, Object> paramMap = new HashMap<>();

        if (dto.length > 0) {
            for (JiraServerUserResultsDto userDto : dto) {
                paramMap.put(userDto.getDisplayName(), userDto);
            }
            log.info(String.valueOf(paramMap));
        }
    }

    public void jiraServerUserKey() {
        JiraServerUserResultsDto[] dto = tempoServerConnector.getJiraServerUsers();
        Map<String, String> paramMap = new HashMap<>();

        if (dto.length > 0) {
            for (JiraServerUserResultsDto userKeyDto : dto) {
                paramMap.put(userKeyDto.getDisplayName(), userKeyDto.getKey());
            }
            log.info(String.valueOf(paramMap));
        }
    }




}