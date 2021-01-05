package tempoexport.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.CloudTeamResultsDto;
import tempoexport.dto.ServerTeamResultsDto;
import tempoexport.dto.TempoCloudTeamDto;
import tempoexport.dto.TempoServerTeamDto;

@Slf4j
@Service
public class TempoTeamsService {

    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;


    public void migrateTempoTeams() {
        TempoCloudTeamDto dto = tempoCloudConnector.getTempoCloudTeams();
        if (dto.getResults() != null) {
            for (CloudTeamResultsDto cloudTeamResultsDto : dto.getResults()) {
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
}