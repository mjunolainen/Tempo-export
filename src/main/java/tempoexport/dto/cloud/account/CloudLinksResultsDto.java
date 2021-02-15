package tempoexport.dto.cloud.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudLinksResultsDto {
    String self;
    Integer id;
    @JsonProperty("scope")
    CloudLinksScopeDto cloudLinksScopeDto;
}