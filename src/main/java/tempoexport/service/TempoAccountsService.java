package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.cloud.account.*;
import tempoexport.dto.server.account.ServerAccountDto;
import tempoexport.dto.server.account.ServerAccountInsertResponseDto;
import tempoexport.dto.server.account.ServerAccountLinksDto;
import tempoexport.dto.server.account.TempoServerAccountDto;
import tempoexport.dto.server.user.JiraServerUserDto;

@Slf4j
@Service
public class TempoAccountsService {
    @Autowired
    private TempoCloudConnector tempoCloudConnector;
    @Autowired
    private TempoServerConnector tempoServerConnector;
    @Autowired
    private TempoServiceUtil tempoServiceUtil;

    public void migrateTempoAccounts() {

        // Serveri accoundi linkide kustutamine
        TempoServerAccountDto[] getTempoAccountsDto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < getTempoAccountsDto.length; i++) {
            TempoServerAccountDto tempoServerAccountDto = getTempoAccountsDto[i];
            ServerAccountLinksDto[] tempoServerSingleAccountLinks = tempoServerConnector.getTempoServerSingleAccountLinks(tempoServerAccountDto.getId());
            for (int j = 0; j < tempoServerSingleAccountLinks.length; j++) {
                ServerAccountLinksDto tempoServerSingleAccountLink = tempoServerSingleAccountLinks[j];
                tempoServerConnector.deleteTempoServerAccountLinks(tempoServerSingleAccountLink.getId());
                log.info("Link to Project deleted: {} from {}", tempoServerSingleAccountLink.getKey(), tempoServerAccountDto.getKey());
            }
        }

        // Serveri accountide kustutamine
        TempoServerAccountDto[] deleteTempoServerAccountDto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < deleteTempoServerAccountDto.length; i++) {
            TempoServerAccountDto dtoAccount = deleteTempoServerAccountDto[i];
            tempoServerConnector.deleteTempoServerAccounts(dtoAccount.getId());
            log.info("Account {} deleted", dtoAccount.getKey());
        }

        // Serveri accountide migratsioon pilvest serverisse
        TempoCloudAccountDto dto = tempoCloudConnector.getTempoCloudAccounts();
        if (dto.getResults() != null) {
            for (CloudAccountResultsDto cloudAccountResultsDto : dto.getResults()) {
                ServerAccountDto insertDto = new ServerAccountDto();
                BeanUtils.copyProperties(cloudAccountResultsDto, insertDto);

                // Account lead migratsioon
                String cloudLeadDisplayName = cloudAccountResultsDto.getCloudAccountResultsLeadDto().getDisplayName();
                //String serverLeadUserKey = jiraServerUserKey(cloudLeadDisplayName);
                String serverLeadUserKey = tempoServiceUtil.jiraServerUserKey(cloudLeadDisplayName);
                JiraServerUserDto serverAccountLeadDto = new JiraServerUserDto();
                serverAccountLeadDto.setUsername(cloudLeadDisplayName);
                serverAccountLeadDto.setKey(serverLeadUserKey);
                insertDto.setJiraServerLead(serverAccountLeadDto);

                // Account contact migratsioon
                String cloudContactDisplayName = "";
                String serverContactUserKey = "";
                if (cloudAccountResultsDto != null && cloudAccountResultsDto.getCloudAccountResultsContactDto() != null) {
                    cloudContactDisplayName = cloudAccountResultsDto.getCloudAccountResultsContactDto().getDisplayName();
                    //serverContactUserKey = jiraServerUserKey(cloudContactDisplayName);
                    serverContactUserKey = tempoServiceUtil.jiraServerUserKey(cloudContactDisplayName);
                }
                JiraServerUserDto serverAccountContactDto = new JiraServerUserDto();
                serverAccountContactDto.setUsername(cloudContactDisplayName);
                serverAccountContactDto.setKey(serverContactUserKey);
                insertDto.setJiraServerContact(serverAccountContactDto);

                // Account customer migratsioon
                if (cloudAccountResultsDto != null && cloudAccountResultsDto.getCloudAccountResultsCustomerDto() != null) {
                    String cloudCustomerName = cloudAccountResultsDto.getCloudAccountResultsCustomerDto().getName();
                    String cloudCustomerKey = cloudAccountResultsDto.getCloudAccountResultsCustomerDto().getKey();

                    CloudAccountResultsCustomerDto serverAccountCustomerDto = new CloudAccountResultsCustomerDto();
                    serverAccountCustomerDto.setKey(cloudCustomerKey);
                    serverAccountCustomerDto.setName(cloudCustomerName);
                    insertDto.setJiraServerCustomer(serverAccountCustomerDto);

                    log.info("Customer {} for account {} created", cloudCustomerName, insertDto.getKey());
                }

                ServerAccountInsertResponseDto tempoServerAccount = tempoServerConnector.insertAccount(insertDto);
                log.info("Account {} created.", insertDto.getKey());

                // Account links migratsioon
                String tempoCloudLinksApi = cloudAccountResultsDto.getCloudAccountResultsLinksDto().getSelf();
                CloudLinksDto tempoCloudLinksDto = tempoCloudConnector.getTempoCloudAccountLinks(tempoCloudLinksApi);

                if (tempoCloudLinksDto.getResults() != null) {
                    for (CloudLinksResultsDto cloudLinksResultsDto : tempoCloudLinksDto.getResults()) {
                        ServerAccountLinksDto insertLinksDto = new ServerAccountLinksDto();
                        insertLinksDto.setAccountId(tempoServerAccount.getId());
                        insertLinksDto.setKey(tempoServerAccount.getKey());
                        insertLinksDto.setLinkType("MANUAL");
                        insertLinksDto.setScope(cloudLinksResultsDto.getCloudLinksScopeDto().getId());
                        insertLinksDto.setScopeType(cloudLinksResultsDto.getCloudLinksScopeDto().getType());
                        tempoServerConnector.insertLinks(insertLinksDto);
                        log.info("Link to Project inserted: {} to {}", insertLinksDto.getScope(), insertDto.getKey());
                    }
                }
            }
        }
    }
}