package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "self",
        "accountId",
        "displayName"
})
public class CloudResultsLeadDto {

        @JsonProperty("self")
        String self;
        @JsonProperty("accountId")
        String accountId;
        @JsonProperty("displayName")
        String displayName;
        @JsonIgnore
        Map<String, Object> resultsLeadProperties = new HashMap<String, Object>();
    }

