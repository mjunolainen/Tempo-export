package tempoexport.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoServerConnector;
import tempoexport.dto.server.user.JiraServerUserResultsDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TempoServiceUtil {
    @Autowired
    private TempoServerConnector tempoServerConnector;
    Map<String, JiraServerUserResultsDto> jiraUserServerMap = null;

    public String getJiraServerUserKey(String cloudDisplayName) {
        String serverUserKey = null;
        if (getJiraServerUserMap().containsKey(cloudDisplayName)) {
            serverUserKey = getJiraServerUserMap().get(cloudDisplayName).getKey();
        }
        return serverUserKey;
    }

    public String getJiraUserName(String cloudDisplayName) {
        String serverUserName = null;
        if (getJiraServerUserMap().containsKey(cloudDisplayName)) {
            serverUserName = getJiraServerUserMap().get(cloudDisplayName).getName();
        }
        return serverUserName;
    }

    public Map<String, JiraServerUserResultsDto> getJiraServerUserMap() {
        if (jiraUserServerMap == null) {
            JiraServerUserResultsDto[] dto = tempoServerConnector.getJiraServerUsers();
            Map<String, JiraServerUserResultsDto> paramMap = new HashMap<>();

            if (dto.length > 0) {
                for (JiraServerUserResultsDto userKeyDto : dto) {
                    paramMap.put(userKeyDto.getDisplayName(), userKeyDto);
                }
            }
            jiraUserServerMap = paramMap;
            return jiraUserServerMap;
        } else {
            return jiraUserServerMap;
        }
    }

    public String getJiraServerUserEmail(String cloudDisplayName) {
        String serverUserEmail = null;
        if (getJiraServerUserMap().containsKey(cloudDisplayName)) {
            serverUserEmail = getJiraServerUserMap().get(cloudDisplayName).getEmailAddress();
        }
        return serverUserEmail;
    }
}
