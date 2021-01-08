package tempoexport.dto.cloud.team.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.cloud.team.CloudTeamResultsUserDto;
import tempoexport.dto.cloud.team.CloudTeamResultsSelfDto;

@Data
public class CloudTeamMemberDto {
    String self;
    @JsonProperty("team")
    CloudTeamResultsSelfDto cloudTeamMemberResultsTeamDto;
    @JsonProperty("member")
    CloudTeamResultsUserDto cloudTeamResultsMemberDto;
    @JsonProperty("memberships")
    CloudTeamResultsMembershipsDto cloudTeamResultsMembershipsDto;
}