package tempoexport.dto.server.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.server.user.JiraServerUserResultsDto;

import java.util.List;

@Data
public class JiraServerUsersDto {
    @JsonProperty("results")
    List<JiraServerUserResultsDto> results = null;
}
