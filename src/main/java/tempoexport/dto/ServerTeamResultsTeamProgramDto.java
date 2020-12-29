package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerTeamResultsTeamProgramDto {
    int id;
    @JsonProperty("manager")
    ServerTempoUserDto serverTempoUserDto;
    String name;
}