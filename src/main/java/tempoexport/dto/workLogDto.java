package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WorkLogDto {

    @JsonProperty("metadata")
    MetaDataDto metaData;
    String self;
}
