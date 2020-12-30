package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.dto.*;
import tempoexport.connector.TempoServerConnector;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class TempoExportService {

    @Autowired
    private RestTemplate restTemplate;
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
        log.info("Results {}", dto.length);

        if (dto.length > 0) {
            for (JiraServerUserResultsDto dtoHashMap : dto) {
                //log.info(dtoHashMap.getKey());
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("key", dtoHashMap.getKey());
                log.info(String.valueOf(paramMap));
            }

        }
    }
}