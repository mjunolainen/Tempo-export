package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TempoCloudAccountDto {

    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    String self;
}