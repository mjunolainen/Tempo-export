package tempoexport.dto.cloud.team.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudTeamResultsMembershipsActiveDto {
    String self;
    Integer id;
    String commitmentPercent;
    String from;
    String to;
    @JsonProperty("role")
    CloudTeamResultsMembershipsActiveRoleDto cloudTeamResultsMembershipsActiveRoleDto;
}