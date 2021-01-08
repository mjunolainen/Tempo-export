package tempoexport.dto.server.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ServerTeamsListDto {
    @JsonProperty("results")
    List<ServerTeamDto> results = null;
}