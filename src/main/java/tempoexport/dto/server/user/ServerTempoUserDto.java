package tempoexport.dto.server.user;

import lombok.Data;

@Data
public class ServerTempoUserDto {
    String avatar;
    String displayname;
    boolean jiraUser;
    String key;
    String name;
}