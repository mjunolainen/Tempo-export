package tempoexport.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.worklog.CloudWorklogDto;
import tempoexport.dto.cloud.worklog.CloudWorklogsListDto;
import tempoexport.dto.server.worklog.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Service
public class TempoWorklogService {
    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;
    @Autowired
    private TempoServiceUtil tempoServiceUtil;

    public void migrateTempoWorklogs() {
        migrateWorklogs();
    }

    public void deleteTempoServerWorklogs() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        TempoServerWorklogRequestDto tempoServerWorklogRequestDto = new TempoServerWorklogRequestDto();
        log.info("From: {}", tempoServerWorklogRequestDto.getFrom());
        log.info("To: {}", tempoServerWorklogRequestDto.getTo());

        while (LocalDate.parse(tempoServerWorklogRequestDto.getFrom(), formatter).isBefore(LocalDate.now())) {

            TempoServerReturnWorklogDto[] tempoServerReturnWorklogDto = tempoServerConnector.getTempoServerWorklogs(tempoServerWorklogRequestDto);
            log.info("Worklog count: {}", tempoServerReturnWorklogDto.length);
            for (TempoServerReturnWorklogDto tempoServerWorklog : tempoServerReturnWorklogDto) {
                log.info(tempoServerWorklog.getTempoWorklogId().toString());
                // tempoServerConnector.deleteTempoServerWorklog(tempoServerWorklog.getTempoWorklogId());
                // log.info("Worklog {} deleted", tempoServerWorklog.getTempoWorklogId());
            }

            LocalDate currentDateTo = LocalDate.parse(tempoServerWorklogRequestDto.getTo(), formatter);

            String nextDateFrom = currentDateTo.toString();
            String nextDateTo = currentDateTo.plusMonths(1).toString();

            tempoServerWorklogRequestDto.setFrom(nextDateFrom);
            tempoServerWorklogRequestDto.setTo(nextDateTo);

            log.info("From: {}", tempoServerWorklogRequestDto.getFrom());
            log.info("To: {}", tempoServerWorklogRequestDto.getTo());
        }
    }

    public void migrateWorklogs() {
        CloudWorklogsListDto tempoCloudWorklogsList = tempoCloudConnector.getTempoCloudWorklogs();

        //TODO tõsta while loopist välja ja kontrolli, kas worklogide serverisse lisamine töötab
        //while (tempoCloudWorklogsList.getCloudWorklogsMetaDataDto().getNext() != null) {
        for (CloudWorklogDto cloudWorklogDto : tempoCloudWorklogsList.getResults()) {
            ServerWorklogDto serverWorklogDto = new ServerWorklogDto();

            serverWorklogDto.setBillableSeconds(cloudWorklogDto.getBillableSeconds());
            serverWorklogDto.setComment(cloudWorklogDto.getDescription());
            if (serverWorklogDto.getComment() == null) {
                serverWorklogDto.setComment(".");
            }
            serverWorklogDto.setStarted(cloudWorklogDto.getStartDate());
            serverWorklogDto.setTimeSpentSeconds(cloudWorklogDto.getTimeSpentSeconds());
            serverWorklogDto.setOriginTaskId(cloudWorklogDto.getCloudWorklogIssueDto().getKey());
            serverWorklogDto.setWorker(tempoServiceUtil.getJiraServerUserKey(cloudWorklogDto.getCloudWorklogAuthorDto().getDisplayName()));
            log.info(serverWorklogDto.toString());

            ServerWorklogInsertResponseDto[] tempoServerWorklog = tempoServerConnector.insertTempoServerWorklog(serverWorklogDto);
            log.info("Worklog for task {} created", serverWorklogDto.getOriginTaskId());
        }
        log.info(tempoCloudWorklogsList.getCloudWorklogsMetaDataDto().getNext());
        tempoCloudWorklogsList = tempoCloudConnector.getNextTempoCloudWorklogs(tempoCloudWorklogsList.getCloudWorklogsMetaDataDto().getNext());
    }
}