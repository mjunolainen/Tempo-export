package tempoexport.dto.server.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.server.user.JiraServerUserDto;

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