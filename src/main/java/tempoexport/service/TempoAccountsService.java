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

    public String migrateTempoAccounts() {

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

        TempoServerAccountDto[] tempoServerAccountsDto = tempoServerConnector.getTempoServerAccounts();
        for (int i = 0; i < tempoServerAccountsDto.length; i++) {
            TempoServerAccountDto tempoServerAccountDto = tempoServerAccountsDto[i];
            tempoServerConnector.deleteTempoServerAccounts(tempoServerAccountDto.getId());
            log.info("Account {} deleted", tempoServerAccountDto.getKey());
        }

        // Tempo account migration
        TempoCloudAccountDto tempoCloudAccountsDto = tempoCloudConnector.getTempoCloudAccounts();
        if (tempoCloudAccountsDto.getResults() != null) {
            for (CloudAccountResultsDto tempoCloudAccountDto : tempoCloudAccountsDto.getResults()) {
                ServerAccountDto tempoServerAccountDto = new ServerAccountDto();
                BeanUtils.copyProperties(tempoCloudAccountDto, tempoServerAccountDto);

                // Tempo account lead migration
                String cloudAccountLeadDisplayName = tempoCloudAccountDto.getCloudAccountResultsLeadDto().getDisplayName();
                String serverAccountLeadKey = tempoServiceUtil.getJiraServerUserKey(cloudAccountLeadDisplayName);
                JiraServerUserDto serverAccountLeadDto = new JiraServerUserDto();
                serverAccountLeadDto.setUsername(cloudAccountLeadDisplayName);
                serverAccountLeadDto.setKey(serverAccountLeadKey);
                tempoServerAccountDto.setJiraServerLead(serverAccountLeadDto);

                // Tempo account contact migration
                String cloudContactDisplayName = "";
                String serverContactUserKey = "";
                if (tempoCloudAccountDto != null && tempoCloudAccountDto.getCloudAccountResultsContactDto() != null) {
                    cloudContactDisplayName = tempoCloudAccountDto.getCloudAccountResultsContactDto().getDisplayName();
                    serverContactUserKey = tempoServiceUtil.getJiraServerUserKey(cloudContactDisplayName);
                }
                JiraServerUserDto serverAccountContactDto = new JiraServerUserDto();
                serverAccountContactDto.setUsername(cloudContactDisplayName);
                serverAccountContactDto.setKey(serverContactUserKey);
                tempoServerAccountDto.setJiraServerContact(serverAccountContactDto);

                // Account customer migratsioon
                if (tempoCloudAccountDto != null && tempoCloudAccountDto.getCloudAccountResultsCustomerDto() != null) {
                    String cloudCustomerName = tempoCloudAccountDto.getCloudAccountResultsCustomerDto().getName();
                    String cloudCustomerKey = tempoCloudAccountDto.getCloudAccountResultsCustomerDto().getKey();

                    CloudAccountResultsCustomerDto serverAccountCustomerDto = new CloudAccountResultsCustomerDto();
                    serverAccountCustomerDto.setKey(cloudCustomerKey);
                    serverAccountCustomerDto.setName(cloudCustomerName);
                    tempoServerAccountDto.setJiraServerCustomer(serverAccountCustomerDto);

                    log.info("Customer {} for account {} created", cloudCustomerName, tempoServerAccountDto.getKey());
                }

                ServerAccountInsertResponseDto tempoServerAccount = tempoServerConnector.insertTempoServerAccount(tempoServerAccountDto);
                log.info("Account {} created.", tempoServerAccountDto.getKey());

                // Tempo account links migration
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
                        log.info("Link to Project inserted: {} to {}", insertLinksDto.getScope(), tempoServerAccountDto.getKey());
                    }
                }
            }
        }
        return "Accounts migrated";
    }
}
