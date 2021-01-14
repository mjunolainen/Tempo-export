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

    }

    public void deleteTempoServerWorklogs() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        TempoServerWorklogRequestDto worklogRequestDates = new TempoServerWorklogRequestDto();
        log.info("From: {}", worklogRequestDates.getFrom());
        log.info("To: {}", worklogRequestDates.getTo());

        while (LocalDate.parse(worklogRequestDates.getFrom(), formatter).isBefore(LocalDate.now())) {
            //TODO hetkel ei ole sysadminil kustutamise õiguseid. Martin peab selle andma.
            TempoServerReturnWorklogDto[] serverWorklogs = tempoServerConnector.getTempoServerWorklogs(worklogRequestDates);
            log.info("Worklog count: {}", serverWorklogs.length);
            for (TempoServerReturnWorklogDto serverWorklog : serverWorklogs) {
                log.info(serverWorklog.getTempoWorklogId().toString());
                // tempoServerConnector.deleteTempoServerWorklog(serverWorklog.getTempoWorklogId());
                // log.info("Worklog {} deleted", serverWorklog.getTempoWorklogId());
            }

            LocalDate currentDateTo = LocalDate.parse(worklogRequestDates.getTo(), formatter);
            String nextDateFrom = currentDateTo.toString();
            String nextDateTo = currentDateTo.plusMonths(1).toString();

            worklogRequestDates.setFrom(nextDateFrom);
            worklogRequestDates.setTo(nextDateTo);

            log.info("From: {}", worklogRequestDates.getFrom());
            log.info("To: {}", worklogRequestDates.getTo());
        }
    }

    public void migrateWorklogs() {
        CloudWorklogsListDto cloudWorklogsListDto = tempoCloudConnector.getTempoCloudWorklogs();

        //TODO tõsta while loopist välja ja kontrolli, kas worklogide serverisse lisamine töötab
        //while (cloudWorklogsListDto.getCloudWorklogsMetaDataDto().getNext() != null) {
        for (CloudWorklogDto cloudWorklogDto : cloudWorklogsListDto.getResults()) {
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
        log.info(cloudWorklogsListDto.getCloudWorklogsMetaDataDto().getNext());
        cloudWorklogsListDto = tempoCloudConnector.getNextTempoCloudWorklogs(cloudWorklogsListDto.getCloudWorklogsMetaDataDto().getNext());
    }
}