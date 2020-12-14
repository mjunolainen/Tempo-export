package Tempoexport.service;

import Tempoexport.repository.TempoExportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TempoExportService {

    @Autowired
     TempoExportRepository tempoExportRepository;

    @Value("fcg42Im2JmExadbA31ZaWHTFzlK5Ll")
    private String bearer;

    @Value("https://api.tempo.io/core/3/worklogs")
    private String address;


}
