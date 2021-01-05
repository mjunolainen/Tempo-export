package tempoexport.dto.server.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.server.team.ServerTeamResultsDto;

import java.util.List;

@Data
public class TempoServerTeamDto {
    @JsonProperty("results")
    List<ServerTeamResultsDto> results = null;
}