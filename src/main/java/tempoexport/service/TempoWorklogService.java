package tempoexport.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.worklog.CloudWorklogDto;
import tempoexport.dto.cloud.worklog.CloudWorklogsListDto;
import tempoexport.dto.server.team.ServerTeamDto;
import tempoexport.dto.server.worklog.ServerWorklogDto;
import tempoexport.dto.server.worklog.ServerWorklogId;

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

        //TODO kontrolli serveri worklogide kustutamist

        // Serveri worklogide kustutamine
        /*ServerWorklogId[] tempoServerWorklogs = tempoServerConnector.getTempoServerWorklogs();
        for (int i = 0; i < tempoServerWorklogs.length; i++) {
            ServerWorklogId worklogDto = tempoServerWorklogs[i];
            tempoServerConnector.deleteTempoServerWorklogs(worklogDto.getWorklogId());
            log.info("Worklog #{} deleted", worklogDto.getWorklogId());
        }*/

        //TODO cloud worklog dto
        //TODO get worklogs from cloud



        CloudWorklogsListDto tempoCloudWorklogsList = tempoCloudConnector.getTempoCloudWorklogs();
        //log.info(tempoCloudWorklogsList.toString());
        log.info(tempoCloudWorklogsList.getSelf());
        log.info(tempoCloudWorklogsList.getCloudWorklogsMetaDataDto().getCount().toString());
        log.info("Next url: {}", tempoCloudWorklogsList.getCloudWorklogsMetaDataDto().getNext());

        if (tempoCloudWorklogsList.getResults() != null) {
            for (CloudWorklogDto cloudWorklogDto : tempoCloudWorklogsList.getResults()){
                ServerWorklogDto serverWorklogDto = new ServerWorklogDto();

                serverWorklogDto.setBillableSeconds(cloudWorklogDto.getBillableSeconds());
                serverWorklogDto.setComment(cloudWorklogDto.getDescription());
                serverWorklogDto.setStarted(cloudWorklogDto.getStartDate() + " " + cloudWorklogDto.getStartTime());
                serverWorklogDto.setTimeSpentSeconds(cloudWorklogDto.getTimeSpentSeconds());
                serverWorklogDto.setOriginTaskId(cloudWorklogDto.getCloudWorklogIssueDto().getKey());
                //serverWorklogDto.setWorker(tempoServiceUtil.getJiraServerUserKey(cloudWorklogDto.getCloudWorklogAuthorDto().getDisplayName()));
                serverWorklogDto.setWorker("Test");
                log.info(serverWorklogDto.toString());
            }
        }

        //TODO server worklog dto
        //TODO cloud worklog dto --> server worklog dto

        //TODO worklog to server
        // TODO offset külge
    }
}