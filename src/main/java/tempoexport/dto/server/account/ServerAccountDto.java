package tempoexport.dto.server.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tempoexport.dto.cloud.account.CloudAccountResultsCustomerDto;
import tempoexport.dto.server.user.JiraServerUserDto;

@Data
public class ServerAccountDto {
    @JsonProperty("contact")
    JiraServerUserDto jiraServerContact;
    @JsonProperty("customer")
    CloudAccountResultsCustomerDto jiraServerCustomer;
    String key;
    @JsonProperty("lead")
    JiraServerUserDto jiraServerLead;
    String name;
    String status;
}