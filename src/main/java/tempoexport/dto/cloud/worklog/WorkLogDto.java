package tempoexport.dto.cloud.worklog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.MetaDataDto;

@Data
public class WorkLogDto {

    @JsonProperty("metadata")
    MetaDataDto metaData;
    String self;
}