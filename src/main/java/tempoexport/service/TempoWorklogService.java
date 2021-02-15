package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.worklog.CloudWorklogDto;
import tempoexport.dto.cloud.worklog.CloudWorklogsListDto;
import tempoexport.dto.server.worklog.ServerWorklogDto;
import tempoexport.dto.server.worklog.TempoServerReturnWorklogDto;
import tempoexport.dto.server.worklog.TempoServerWorklogRequestDto;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Value("${jira.cloud.get.worklogs.count}")
    private Integer cloudWorklogsCount;

    public Integer worklogCount = 0;

    public String deleteTempoServerWorklogs() {
        LocalTime timeStart = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        TempoServerWorklogRequestDto worklogRequestDates = new TempoServerWorklogRequestDto();
        log.info("From: {}", worklogRequestDates.getFrom());
        log.info("To: {}", worklogRequestDates.getTo());

        while (LocalDate.parse(worklogRequestDates.getFrom(), formatter).isBefore(LocalDate.now())) {
            TempoServerReturnWorklogDto[] serverWorklogs = tempoServerConnector.getTempoServerWorklogs(worklogRequestDates);
            log.info("Period worklog count: {}", serverWorklogs.length);
            worklogCount = worklogCount + serverWorklogs.length;
            for (TempoServerReturnWorklogDto serverWorklog : serverWorklogs) {
                tempoServerConnector.deleteTempoServerWorklog(serverWorklog.getTempoWorklogId());
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    log.error("Siia loodetavasti ei jõua kunagi");
                    e.printStackTrace();
                }
            }

            LocalDate currentDateTo = LocalDate.parse(worklogRequestDates.getTo(), formatter);
            String nextDateFrom = currentDateTo.toString();
            String nextDateTo = currentDateTo.plusMonths(1).toString();

            worklogRequestDates.setFrom(nextDateFrom);
            worklogRequestDates.setTo(nextDateTo);

            log.info("Deleted worklogs: {}", tempoServerConnector.deletedServerWorklogs);
            log.info("Worklogs left in server: {}", tempoServerConnector.worklogsNotDeletedFromServer);
            log.info("From: {}", worklogRequestDates.getFrom());
            log.info("To: {}", worklogRequestDates.getTo());
        }
        LocalTime timeEnd = LocalTime.now();

        log.info("Start time: {}", timeStart);
        log.info("End time: {}", timeEnd);

        tempoServerConnector.serverWorklogDeletionErrorCounter500 = 0;
        tempoServerConnector.serverWorklogDeletionErrorCounter403 = 0;

        return "Worklogs deleted";
    }

    public String migrateWorklogs() {
        LocalTime timeStart = LocalTime.now();
        Integer worklogCountFromCloud = 0;
        Integer serverWorklogUsersWithoutName = 0;


        CloudWorklogsListDto cloudWorklogsListDto = tempoCloudConnector.getTempoCloudWorklogs();
        log.info(cloudWorklogsListDto.getSelf());

        while (cloudWorklogsListDto.getCloudWorklogsMetaDataDto().getNext() != null) {
            worklogCountFromCloud = worklogCountFromCloud + cloudWorklogsListDto.getCloudWorklogsMetaDataDto().getCount();

            for (CloudWorklogDto cloudWorklogDto : cloudWorklogsListDto.getResults()) {
                ServerWorklogDto serverWorklogDto = new ServerWorklogDto();

                serverWorklogDto.setBillableSeconds(cloudWorklogDto.getBillableSeconds());
                serverWorklogDto.setComment(cloudWorklogDto.getDescription());

                if (serverWorklogDto.getComment() == null || serverWorklogDto.getComment().isEmpty()) {
                    serverWorklogDto.setComment(".");
                }

                serverWorklogDto.setStarted(cloudWorklogDto.getStartDate());
                serverWorklogDto.setTimeSpentSeconds(cloudWorklogDto.getTimeSpentSeconds());
                serverWorklogDto.setOriginTaskId(cloudWorklogDto.getCloudWorklogIssueDto().getKey());
                serverWorklogDto.setWorker(tempoServiceUtil.getJiraServerUserKey(cloudWorklogDto.getCloudWorklogAuthorDto().getDisplayName()));

                if (serverWorklogDto.getWorker() != null) {
                    tempoServerConnector.insertTempoServerWorklog(serverWorklogDto);
                } else {
                    serverWorklogUsersWithoutName++;
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    log.error("Siia loodetavasti ei jõua kunagi");
                    e.printStackTrace();
                }
            }
            log.info(cloudWorklogsListDto.getCloudWorklogsMetaDataDto().getNext());
            cloudWorklogsListDto = tempoCloudConnector.getNextTempoCloudWorklogs(cloudWorklogsListDto.getCloudWorklogsMetaDataDto().getNext());
        }
        LocalTime timeEnd = LocalTime.now();

        log.info("Start time: {}", timeStart);
        log.info("End time: {}", timeEnd);

        return "Worklogs migrated";
    }
}