package tempoexport.dto.cloud.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudAccountLinksResultsDto {
    String self;
    Integer id;
    @JsonProperty("scope")
    CloudAccountLinksScopeDto cloudAccountLinksScopeDto;
    @JsonProperty("account")
    CloudAccountResultsLinksDto cloudAccountLinksAccountDto;
}
