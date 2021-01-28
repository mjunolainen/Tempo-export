package tempoexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tempoexport.service.TempoAccountsService;
import tempoexport.service.TempoTeamsService;
import tempoexport.service.TempoWorklogService;

@RestController
public class TempoExportController {
    @Autowired
    private TempoAccountsService tempoAccountsService;
    @Autowired
    private TempoTeamsService tempoTeamsService;
    @Autowired
    private TempoWorklogService tempoWorklogService;

    @GetMapping("migrateTempoAccounts")
    public void getMigrateTempoAccounts() { tempoAccountsService.migrateTempoAccounts(); }

    @GetMapping("migrateTempoTeams")
    public void migrateTempoTeams() {tempoTeamsService.migrateTempoTeams(); }

    @GetMapping("deleteTempoServerWorklogs")
    public String getTempoServerWorklogs() {return tempoWorklogService.deleteTempoServerWorklogs(); }

    @GetMapping("migrateWorklogs")
    public String migrateWorklogs() {return tempoWorklogService.migrateWorklogs(); }
}