package tempoexport.dto.server.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempoServerAccountDto {
    Integer id;
    String key;
    @JsonProperty("links")
    List<ServerAccountLinksDto> links;
}