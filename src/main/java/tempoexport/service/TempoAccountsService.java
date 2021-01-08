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
        TempoServerAccountDto[] tempoServerAccounts = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < tempoServerAccounts.length; i++) {
            TempoServerAccountDto tempoServerAccountDto = tempoServerAccounts[i];
            ServerAccountLinksDto[] tempoServerAccountLinks = tempoServerConnector.getTempoServerSingleAccountLinks(tempoServerAccountDto.getId());
            for (int j = 0; j < tempoServerAccountLinks.length; j++) {
                ServerAccountLinksDto tempoServerAccountLink = tempoServerAccountLinks[j];
                tempoServerConnector.deleteTempoServerAccountLinks(tempoServerAccountLink.getId());
                log.info("Link to Project deleted: {} from {}", tempoServerAccountLink.getKey(), tempoServerAccountDto.getKey());
            }
        }

        // Serveri accountide kustutamine
        TempoServerAccountDto[] tempoServerAccountsDto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < tempoServerAccountsDto.length; i++) {
            TempoServerAccountDto tempoServerAccountDto = tempoServerAccountsDto[i];
            tempoServerConnector.deleteTempoServerAccounts(tempoServerAccountDto.getId());
            log.info("Account {} deleted", tempoServerAccountDto.getKey());
        }

        // Serveri accountide migratsioon pilvest serverisse
        TempoCloudAccountDto tempoCloudAccountsDto = tempoCloudConnector.getTempoCloudAccounts();
        if (tempoCloudAccountsDto.getResults() != null) {
            for (CloudAccountResultsDto tempoCloudAccountDto : tempoCloudAccountsDto.getResults()) {
                ServerAccountDto insertTempoServerAccountDto = new ServerAccountDto();
                BeanUtils.copyProperties(tempoCloudAccountDto, insertTempoServerAccountDto);

                // Account lead migratsioon
                String cloudAccountLeadDisplayName = tempoCloudAccountDto.getCloudAccountResultsLeadDto().getDisplayName();
                String serverAccountLeadKey = tempoServiceUtil.jiraServerUserKey(cloudAccountLeadDisplayName);
                JiraServerUserDto serverAccountLeadDto = new JiraServerUserDto();
                serverAccountLeadDto.setUsername(cloudAccountLeadDisplayName);
                serverAccountLeadDto.setKey(serverAccountLeadKey);
                insertTempoServerAccountDto.setJiraServerLead(serverAccountLeadDto);

                // Account contact migratsioon
                String cloudContactDisplayName = "";
                String serverContactUserKey = "";
                if (tempoCloudAccountDto != null && tempoCloudAccountDto.getCloudAccountResultsContactDto() != null) {
                    cloudContactDisplayName = tempoCloudAccountDto.getCloudAccountResultsContactDto().getDisplayName();
                    //serverContactUserKey = jiraServerUserKey(cloudContactDisplayName);
                    serverContactUserKey = tempoServiceUtil.jiraServerUserKey(cloudContactDisplayName);
                }
                JiraServerUserDto serverAccountContactDto = new JiraServerUserDto();
                serverAccountContactDto.setUsername(cloudContactDisplayName);
                serverAccountContactDto.setKey(serverContactUserKey);
                insertTempoServerAccountDto.setJiraServerContact(serverAccountContactDto);

                // Account customer migratsioon
                if (tempoCloudAccountDto != null && tempoCloudAccountDto.getCloudAccountResultsCustomerDto() != null) {
                    String cloudCustomerName = tempoCloudAccountDto.getCloudAccountResultsCustomerDto().getName();
                    String cloudCustomerKey = tempoCloudAccountDto.getCloudAccountResultsCustomerDto().getKey();

                    CloudAccountResultsCustomerDto serverAccountCustomerDto = new CloudAccountResultsCustomerDto();
                    serverAccountCustomerDto.setKey(cloudCustomerKey);
                    serverAccountCustomerDto.setName(cloudCustomerName);
                    insertTempoServerAccountDto.setJiraServerCustomer(serverAccountCustomerDto);

                    log.info("Customer {} for account {} created", cloudCustomerName, insertTempoServerAccountDto.getKey());
                }

                ServerAccountInsertResponseDto tempoServerAccount = tempoServerConnector.insertTempoServerAccount(insertTempoServerAccountDto);
                log.info("Account {} created.", insertTempoServerAccountDto.getKey());

                // Account links migratsioon
                String tempoCloudLinksApi = tempoCloudAccountDto.getCloudAccountResultsLinksDto().getSelf();
                CloudLinksDto tempoCloudLinksDto = tempoCloudConnector.getTempoCloudAccountLinks(tempoCloudLinksApi);

                if (tempoCloudLinksDto.getResults() != null) {
                    for (CloudLinksResultsDto cloudLinksResultsDto : tempoCloudLinksDto.getResults()) {
                        ServerAccountLinksDto insertLinksDto = new ServerAccountLinksDto();
                        insertLinksDto.setAccountId(tempoServerAccount.getId());
                        insertLinksDto.setKey(tempoServerAccount.getKey());
                        insertLinksDto.setLinkType("MANUAL");
                        insertLinksDto.setScope(cloudLinksResultsDto.getCloudLinksScopeDto().getId());
                        insertLinksDto.setScopeType(cloudLinksResultsDto.getCloudLinksScopeDto().getType());
                        tempoServerConnector.insertTempoServerAccountLinks(insertLinksDto);
                        log.info("Link to Project inserted: {} to {}", insertLinksDto.getScope(), insertTempoServerAccountDto.getKey());
                    }
                }
            }
        }
    }
}