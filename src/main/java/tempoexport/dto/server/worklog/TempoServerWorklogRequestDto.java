package tempoexport.dto.server.worklog;

import lombok.Data;

@Data
public class TempoServerWorklogRequestDto {
    String from = "0001-01-01";
    String to = "2015-12-31";
}