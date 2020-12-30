package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class JiraServerUsersDto {
    @JsonProperty("results")
    List<JiraServerUserResultsDto> results = null;


}
