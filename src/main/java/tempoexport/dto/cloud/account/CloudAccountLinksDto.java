package tempoexport.dto.cloud.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CloudAccountLinksDto {

    String self;
    Integer id;
    @JsonProperty("scope")
    List<CloudAccountLinksScopeDto> scope = null;

}
