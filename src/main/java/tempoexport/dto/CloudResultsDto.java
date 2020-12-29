package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudResultsDto {

    String self;
    String key;
    int id;
    String name;
    String status;
    boolean global;
    @JsonProperty("lead")
    CloudResultsLeadDto cloudResultsLeadDto;
    @JsonProperty("links")
    CloudResultsLinksDto cloudResultsLinksDto;
}