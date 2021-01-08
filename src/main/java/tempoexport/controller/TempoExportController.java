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

    @GetMapping("migrateTempoTeams")
    public void migrateTempoTeams() {tempoTeamsService.migrateTempoTeams(); }

    // Siit edasi testimiseks. Hiljem kustuta ära
    @GetMapping("tempoCloudTeamMembers")
    public void getTempoCloudTeams() {
        tempoTeamsService.tempoCloudTeamMembers();
    }
}