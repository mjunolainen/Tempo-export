package tempoexport.dto.cloud.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CloudLinksDto {
    String self;
    @JsonProperty("metadata")
    CloudMetaDataDto metaData;
    @JsonProperty("results")
    List<CloudLinksResultsDto> results = null;
}