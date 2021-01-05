package tempoexport.dto.server.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.server.user.ServerTempoUserDto;

@Data
public class ServerTeamResultsTeamProgramDto {
    int id;
    @JsonProperty("manager")
    ServerTempoUserDto serverTempoUserDto;
    String name;
}