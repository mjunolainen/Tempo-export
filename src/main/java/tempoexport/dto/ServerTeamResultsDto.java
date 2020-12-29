package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
