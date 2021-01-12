package tempoexport.dto.server.worklog;

import lombok.Data;

@Data
public class ServerWorklogInsertResponseDto {
    Integer id;
    Integer issueId;
    Integer jiraWorklogId;
    String workerKey;
    Integer billableSeconds;
    Integer timeSpentSeconds;
    String startDate;
    String comment;
}