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
        "key",
        "id",
        "name",
        "status",
        "global",
        "lead",
        "links"
})
public class CloudResultsDto {

    @JsonProperty("self")
    String self;
    @JsonProperty("key")
    String key;
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
    @JsonProperty("status")
    String status;
    @JsonProperty("global")
    boolean global;
    @JsonProperty("lead")
    CloudResultsLeadDto resultsLeadProperties;
    @JsonProperty("links")
    CloudResultsLinksDto resultsLinksProperties;
    @JsonIgnore
    Map<String, Object> resultsProperties = new HashMap<String, Object>();

}