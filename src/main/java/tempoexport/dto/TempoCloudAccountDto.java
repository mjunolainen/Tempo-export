package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempoCloudAccountDto {

    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    String self;

    @JsonProperty("results")
    List<CloudResultsDto> results = null;
}