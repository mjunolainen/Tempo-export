package tempoexport.dto.cloud.team.members;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.cloud.account.CloudMetaDataDto;

import java.util.List;

@Data
public class CloudTeamMembersDto {
    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    String self;
    @JsonProperty("results")
    List<CloudTeamMemberDto> results = null;
}