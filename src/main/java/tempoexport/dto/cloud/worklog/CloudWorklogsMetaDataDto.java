package tempoexport.dto.cloud.worklog;


import lombok.Data;

@Data
public class CloudWorklogsMetaDataDto {
    Integer count;
    Integer offset;
    Integer limit;
    String next;
}