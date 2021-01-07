package tempoexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tempoexport.service.TempoAccountsService;
import tempoexport.service.TempoTeamsService;

@RestController
public class TempoExportController {
    @Autowired
    private TempoAccountsService tempoAccountsService;
    @Autowired
    private TempoTeamsService tempoTeamsService;

    @GetMapping("migrateTempoAccounts")
    public void getMigrateTempoAccounts() { tempoAccountsService.migrateTempoAccounts(); }

    @GetMapping("tempoCloudTeams")
    public void getTempoCloudTeams() {
        tempoTeamsService.tempoCloudTeams();
    }

    @GetMapping("tempoServerTeams")
    public void getTempoServerTeams() {
        tempoTeamsService.tempoServerTeams();
    }

    @GetMapping("jiraServerLeadEmail")
    public void jiraServerLeadEmail() {tempoAccountsService.jiraServerLeadEmail(); }
}