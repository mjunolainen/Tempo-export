package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerAccountDto {

    @JsonProperty("contact")
    JiraServerUserDto jiraServerContact;
    String key;
    @JsonProperty("lead")
    JiraServerUserDto jiraServerLead;
    String name;
    String status;
}