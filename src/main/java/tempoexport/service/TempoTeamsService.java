package tempoexport.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.team.CloudTeamResultsDto;
import tempoexport.dto.cloud.team.TempoCloudTeamDto;
import tempoexport.dto.server.team.TempoServerTeamDto;

@Slf4j
@Service
public class TempoTeamsService {

    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;

    // TODO teamide kustutamine serverist
    // TODO kas enne teamide kustutamist on vaja veel midagi kustutada, et saaks teame kustutada?
    // TODO teamide migratsiooni pilvest serverisse
    // TODO kui teamid on migreeritud, siis sealt edasi - team members. Midagi veel?

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