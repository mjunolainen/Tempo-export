package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class workLogDto {

    @JsonProperty("metadata")
    metaDataDto metaData;
    String self;
}

