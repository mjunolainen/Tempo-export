package tempoexport.dto.cloud.worklog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudWorklogDto {
    String self;
    Integer tempoWorklogId;
    Integer jiraWorklogId;
    @JsonProperty("issue")
    CloudWorklogIssueDto cloudWorklogIssueDto;
    Integer timeSpentSeconds;
    Integer billableSeconds;
    String startDate;
    String startTime;
    String description;
    String createdAt;
    String updatedAt;
    @JsonProperty("author")
    CloudWorklogAuthorDto cloudWorklogAuthorDto;
    @JsonProperty("attributes")
    CloudWorklogAttributesDto cloudWorklogAttributesDto;
}