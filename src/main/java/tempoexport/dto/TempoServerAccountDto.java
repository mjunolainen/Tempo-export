package tempoexport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempoServerAccountDto {
    @JsonProperty("results")
    List<ServerAccountResultsDto> results = null;
}