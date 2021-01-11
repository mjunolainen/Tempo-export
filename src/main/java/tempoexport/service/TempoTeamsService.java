package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.team.CloudTeamDto;
import tempoexport.dto.cloud.team.CloudTeamsListDto;
import tempoexport.dto.cloud.team.members.CloudTeamMemberDto;
import tempoexport.dto.cloud.team.members.CloudTeamMembersDto;
import tempoexport.dto.server.team.ServerTeamInsertResponseDto;
import tempoexport.dto.server.team.ServerTeamDto;
import tempoexport.dto.server.team.members.*;
import tempoexport.dto.server.user.ServerTempoUserDto;

@Slf4j
@Service
public class TempoTeamsService {

    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;
    @Autowired
    private TempoServiceUtil tempoServiceUtil;

    public void migrateTempoTeams() {

        // Serveri teamide kustutamine
        ServerTeamDto[] tempoServerTeams = tempoServerConnector.getTempoServerTeams();
        for (int i = 0; i < tempoServerTeams.length; i++) {
            ServerTeamDto dtoTeam = tempoServerTeams[i];
            tempoServerConnector.deleteTempoServerTeams(dtoTeam.getId());
            log.info("Team {} deleted", dtoTeam.getName());
        }

        // Teami migratsiooni pilvest serverisse
        CloudTeamsListDto tempoCloudTeamsListDto = tempoCloudConnector.getTempoCloudTeams();
        if (tempoCloudTeamsListDto.getResults() != null) {
            for (CloudTeamDto tempoCloudTeamDto : tempoCloudTeamsListDto.getResults()) {
                ServerTeamDto tempoServerTeamDto = new ServerTeamDto();

                tempoServerTeamDto.setName(tempoCloudTeamDto.getName());
                tempoServerTeamDto.setSummary(tempoCloudTeamDto.getSummary());

                ServerTempoUserDto tempoServerTeamLeadDto = new ServerTempoUserDto();
                tempoServerTeamLeadDto.setDisplayname(tempoCloudTeamDto.getCloudTeamResultsUserDto().getDisplayName());
                String serverLeadUserKey = tempoServiceUtil.getJiraServerUserKey(tempoServerTeamLeadDto.getDisplayname());
                tempoServerTeamLeadDto.setKey(serverLeadUserKey);
                tempoServerTeamLeadDto.setJiraUser(true);
                tempoServerTeamLeadDto.setName(tempoServiceUtil.getJiraUserName(serverLeadUserKey));
                tempoServerTeamDto.setServerTempoLeadUserDto(tempoServerTeamLeadDto);
                tempoServerTeamDto.setLead(serverLeadUserKey);

                ServerTeamInsertResponseDto tempoServerTeam = tempoServerConnector.insertTempoServerTeam(tempoServerTeamDto);
                log.info("Team {} created.", tempoServerTeamDto.getName());


                // Tempo team member migration
                String tempoCloudTeamMembersUrl = tempoCloudTeamDto.getCloudTeamResultsMembersDto().getSelf();
                CloudTeamMembersDto tempoCloudTeamMembersListDto = tempoCloudConnector.getTempoCloudTeamMembers(tempoCloudTeamMembersUrl);
                log.info(tempoCloudTeamMembersListDto.getMetaData().getCount().toString());

                if (tempoCloudTeamMembersListDto != null) {
                    for (CloudTeamMemberDto tempoCloudTeamMemberDto : tempoCloudTeamMembersListDto.getResults()) {

                        ServerTeamMemberDto tempoServerTeamMemberDto = new ServerTeamMemberDto();

                        ServerTeamMemberNameDto tempoServerTeamMemberNameDto = new ServerTeamMemberNameDto();
                        if (tempoCloudTeamMemberDto != null &&
                                tempoCloudTeamMemberDto.getCloudTeamResultsMemberDto() != null) {
                            tempoServerTeamMemberNameDto.setName(tempoServiceUtil.getJiraServerUserKey(tempoCloudTeamMemberDto
                                    .getCloudTeamResultsMemberDto().getDisplayName()));
                        }
                        tempoServerTeamMemberNameDto.setType("USER");
                        tempoServerTeamMemberDto.setServerTeamMemberNameDto(tempoServerTeamMemberNameDto);

                        ServerTeamMemberMembershipDto tempoServerTeamMemberMembershipDto = new ServerTeamMemberMembershipDto();
                        ServerTeamMemberRoleDto tempoServerTeamMemberRoleDto = new ServerTeamMemberRoleDto();

                        if (tempoCloudTeamMemberDto != null &&
                                tempoCloudTeamMemberDto.getCloudTeamResultsMembershipsDto() != null &&
                                tempoCloudTeamMemberDto.getCloudTeamResultsMembershipsDto().getCloudTeamResultsMembershipsActiveDto() != null) {
                            tempoServerTeamMemberMembershipDto.setAvailability(tempoCloudTeamMemberDto.getCloudTeamResultsMembershipsDto()
                                    .getCloudTeamResultsMembershipsActiveDto().getCommitmentPercent());
                            tempoServerTeamMemberMembershipDto.setDateFrom(tempoCloudTeamMemberDto.getCloudTeamResultsMembershipsDto()
                                    .getCloudTeamResultsMembershipsActiveDto().getFrom());
                            tempoServerTeamMemberMembershipDto.setDateTo(tempoCloudTeamMemberDto.getCloudTeamResultsMembershipsDto()
                                    .getCloudTeamResultsMembershipsActiveDto().getTo());

                            tempoServerTeamMemberRoleDto.setId(tempoCloudTeamMemberDto.getCloudTeamResultsMembershipsDto()
                                    .getCloudTeamResultsMembershipsActiveDto().getCloudTeamResultsMembershipsActiveRoleDto().getId());
                        } else {
                            tempoServerTeamMemberMembershipDto.setAvailability("");
                            tempoServerTeamMemberMembershipDto.setDateFrom("");
                            tempoServerTeamMemberMembershipDto.setDateTo("");

                            tempoServerTeamMemberRoleDto.setId(0);
                        }

                        tempoServerTeamMemberMembershipDto.setServerTeamMemberRoleDto(tempoServerTeamMemberRoleDto);
                        tempoServerTeamMemberDto.setServerTeamMemberMembershipDto(tempoServerTeamMemberMembershipDto);

                        log.info(tempoServerTeamMemberDto.toString());

                        //TODO team memberid serverisse teamide külge
                        // Server maas - siit edasi testimata kood

                        ServerTeamMemberInsertResponseDto tempoServerTeamMemeber = tempoServerConnector.insertTempoServerTeamMember
                                (tempoServerTeamMemberDto, tempoServerTeam.getId());
                        log.info("Team member {} inserted for team {}",
                                tempoServerTeamMemberDto.getServerTeamMemberNameDto().getName(), tempoCloudTeamDto.getName());
                    }
                }
            }
        }
    }
}