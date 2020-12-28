package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempoServerTeamDto {
    @JsonProperty("results")
    List<ServerTeamResultsDto> results = null;

}
