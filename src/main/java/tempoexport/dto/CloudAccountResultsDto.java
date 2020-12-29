package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudAccountResultsDto {

    String self;
    String key;
    int id;
    String name;
    String status;
    boolean global;
    @JsonProperty("lead")
    CloudAccountResultsLeadDto cloudAccountResultsLeadDto;
    @JsonProperty("customer")
    CloudAccountResultsCustomerDto cloudAccountResultsCustomerDto;
    @JsonProperty("links")
    CloudAccountResultsLinksDto cloudAccountResultsLinksDto;

}