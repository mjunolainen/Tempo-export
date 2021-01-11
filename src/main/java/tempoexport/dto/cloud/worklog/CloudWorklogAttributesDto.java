package tempoexport.dto.cloud.worklog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CloudWorklogAttributesDto {
    String self;
    @JsonProperty("values")
    List<CloudWorklogAttributesValuesDto> values = null;
}