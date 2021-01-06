package tempoexport.dto.server.account;

import lombok.Data;

@Data
public class ServerAccountLinksDto {
    Integer accountId;
    Integer id;
    boolean defaultAccount;
    String key;
    String linkType;
    String name;
    Integer scope;
    String scopeType;
}