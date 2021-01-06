package tempoexport.dto.cloud.account;

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
    @JsonProperty("contact")
    CloudAccountResultsContactDto cloudAccountResultsContactDto;
    @JsonProperty("links")
    CloudAccountResultsLinksDto cloudAccountResultsLinksDto;

}