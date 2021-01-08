package tempoexport.dto.server.team.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerTeamMemberDto {
    @JsonProperty("member")
    ServerTeamMemberNameDto serverTeamMemberNameDto;
    @JsonProperty("membership")
    ServerTeamMemberMembershipDto serverTeamMemberMembershipDto;
}