package tempoexport.dto.server.team.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerTeamMemberInsertResponseDto {
    Integer id;
    @JsonProperty("member")
    ServerTeamMemberNameDto serverTeamMemberNameDto;
    @JsonProperty("membership")
    ServerTeamMemberMembershipDto serverTeamMemberMembershipDto;
}