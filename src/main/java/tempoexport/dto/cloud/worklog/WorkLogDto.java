package tempoexport.dto.cloud.worklog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WorkLogDto {

    @JsonProperty("metadata")
    CloudWorklogsMetaDataDto metaData;
    String self;
}