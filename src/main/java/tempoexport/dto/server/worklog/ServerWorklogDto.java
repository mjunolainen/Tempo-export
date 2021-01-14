package tempoexport.dto.server.worklog;

import lombok.Data;

@Data
public class ServerWorklogDto {
    Integer billableSeconds;
    String comment;
    String endDate;
    String originTaskId;
    Integer remainingEstimate;
    String started;
    Integer timeSpentSeconds;
    String worker;
}
