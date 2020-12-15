package Tempoexport.controller;

        import Tempoexport.service.TempoExportService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempoExportController {
    @Autowired
    private TempoExportService tempoExportService;

    @GetMapping("tempoData")
    public void getTempoData() {
        tempoExportService.tempoData();
    }
}