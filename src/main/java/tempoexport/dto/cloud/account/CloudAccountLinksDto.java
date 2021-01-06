package tempoexport.dto.cloud.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CloudAccountLinksDto {
    String self;
    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    @JsonProperty("results")
    List<CloudAccountLinksResultsDto> results = null;
}