package tempoexport.dto.server.account;

import lombok.Data;

@Data
public class ServerAccountLinksDto {
    Integer accountId;
    boolean defaultAccount;
    String key;
    String linkType;
    String name;
    Integer scope;
    String scopeType;
}