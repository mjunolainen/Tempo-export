package tempoexport.dto.cloud.team.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudTeamResultsMembershipsDto {
    String self;
    @JsonProperty("active")
    CloudTeamResultsMembershipsActiveDto cloudTeamResultsMembershipsActiveDto;
}