package tempoexport.dto.server.worklog;

import lombok.Data;


@Data
public class TempoServerReturnWorklogDto {
    Integer billableSeconds;
    String timeSpent;
    String comment;
    Integer tempoWorklogId;
    String started;
    Integer originTaskId;
    String worker;
}