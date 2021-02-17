package tempoexport.dto.cloud.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.cloud.account.CloudMetaDataDto;
import java.util.List;

@Data
public class CloudTeamsListDto {
    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    String self;
    @JsonProperty("results")
    List<CloudTeamDto> results = null;
}