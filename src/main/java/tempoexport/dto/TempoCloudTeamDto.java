package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempoCloudTeamDto {

    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    String self;

    @JsonProperty("results")
    List<CloudTeamResultsDto> results = null;
}
