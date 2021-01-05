package tempoexport.dto.server.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.server.user.ServerTempoUserDto;

@Data
public class ServerTeamResultsDto {
    int id;
    boolean isPublic;
    String lead;
    @JsonProperty("leadUser")
    ServerTempoUserDto serverTempoUserDto;
    String mission;
    String name;
    String summary;
    @JsonProperty("teamProgram")
    ServerTeamResultsTeamProgramDto serverTeamResultsTeamProgramDto;
}