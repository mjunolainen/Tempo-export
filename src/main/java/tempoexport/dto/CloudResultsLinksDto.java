package tempoexport.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "self"
})
public class CloudResultsLinksDto {

    @JsonProperty("self")
    String self;
    @JsonIgnore
    Map<String, Object> resultsLinksProperties = new HashMap<String, Object>();
}


