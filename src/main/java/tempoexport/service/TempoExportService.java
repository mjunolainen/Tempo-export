package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.dto.TempoCloudAccountDto;
import tempoexport.dto.WorkLogDto;

@Slf4j
@Service
public class TempoExportService {

    @Autowired
    private TempoCloudConnector tempoCloudConnector;

    public void tempoData() {
        WorkLogDto dto = tempoCloudConnector.getWorklogs();
        log.info("Count {}", dto.getMetaData().getCount());
        log.info("Offset {}", dto.getMetaData().getOffset());
        log.info("Limit {}", dto.getMetaData().getLimit());
    }

    public void tempoCloudAccounts() {
        TempoCloudAccountDto dto = tempoCloudConnector.getTempoCloudAccounts();
        log.info("Count {}", dto.getMetaData().getCount());
        log.info("Results {}", dto.getResults());
    }
}
