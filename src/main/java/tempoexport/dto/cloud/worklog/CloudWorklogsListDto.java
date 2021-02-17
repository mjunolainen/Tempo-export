package tempoexport.dto.cloud.worklog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CloudWorklogsListDto {
    String self;
    @JsonProperty("metadata")
    CloudWorklogsMetaDataDto cloudWorklogsMetaDataDto;
    @JsonProperty("results")
    List<CloudWorklogDto> results = null;
}
