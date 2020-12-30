package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class  ServerAccountDto {

  @JsonProperty("jiraServerContact")
  JiraServerUserDto jiraServerContact;
  String key;
  @JsonProperty("jiraServerLead")
  JiraServerUserDto jiraServerLead;
  String name;
  String status;
}
