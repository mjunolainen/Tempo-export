package tempoexport.dto.cloud.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudTeamResultsDto {
    String self;
    int id;
    String name;
    String summary;
    @JsonProperty("lead")
    CloudTeamResultsLeadDto cloudTeamResultsLeadDto;
    @JsonProperty("program")
    CloudTeamResultsProgramDto cloudTeamResultsProgramDto;
    @JsonProperty("links")
    CloudTeamResultsLinksDto cloudTeamResultsLinksDto;
    @JsonProperty("members")
    CloudTeamResultsMembersDto cloudTeamResultsMembersDto;
    @JsonProperty("permissions")
    CloudTeamResultsPermissionsDto cloudTeamResultsPermissionsDto;
}