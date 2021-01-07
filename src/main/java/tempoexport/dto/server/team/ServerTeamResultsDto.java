package tempoexport.dto.server.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.server.user.ServerTempoUserDto;

@Data
public class ServerTeamResultsDto {
    String id;
    String lead;
    @JsonProperty("leadUser")
    ServerTempoUserDto serverTempoLeadUserDto;
    String name;
    String summary;
    @JsonProperty("teamProgram")
    ServerTeamResultsTeamProgramDto serverTeamResultsTeamProgramDto;
}