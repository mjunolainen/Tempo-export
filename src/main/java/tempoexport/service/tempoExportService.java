package tempoexport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tempoexport.connector.TempoCloudConnector;
import tempoexport.dto.WorkLogDto;

@Slf4j
@Service
public class tempoExportService {

    @Autowired
    private TempoCloudConnector tempoCloudConnector;

    public void tempoData() {
      WorkLogDto dto = tempoCloudConnector.getWorklogs();
      log.info("Count {}", dto.getMetaData().getCount());
    }
}
