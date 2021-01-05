package tempoexport.dto.cloud.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.CloudMetaDataDto;

import java.util.List;

@Data
public class TempoCloudTeamDto {

    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    String self;

    @JsonProperty("results")
    List<CloudTeamResultsDto> results = null;
}