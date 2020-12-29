package tempoexport.dto;

import lombok.Data;

@Data
public class ServerAccountResultsDto {
    // kuna seda päringut vaja ainult kustutamiseks ja kustutamine käib id alusel, siis päring ainult id.
    int id;
}