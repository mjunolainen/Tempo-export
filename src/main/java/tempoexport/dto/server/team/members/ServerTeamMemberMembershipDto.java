package tempoexport.dto.server.team.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerTeamMemberMembershipDto {
    String availability;
    String dateFrom;
    String dateTo;
    @JsonProperty("role")
    ServerTeamMemberRoleDto serverTeamMemberRoleDto;
}