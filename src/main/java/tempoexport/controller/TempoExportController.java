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

    //
    @GetMapping("tempoCloudTeams")
    public void getTempoCloudTeams() {
        tempoTeamsService.tempoCloudTeams();
    }

    @GetMapping("tempoServerTeams")
    public void getTempoServerTeams() {
        tempoTeamsService.tempoServerTeams();
    }

    @GetMapping("deleteTempoTeams")
    public void deleteTempoTeams() { tempoTeamsService.deleteTempoTeams(); }
}