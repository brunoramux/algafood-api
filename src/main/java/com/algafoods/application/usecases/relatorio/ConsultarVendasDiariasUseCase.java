package com.algafoods.application.usecases.relatorio;

import com.algafoods.application.dto.VendaDiariaDTO;
import com.algafoods.application.filter.VendaDiariaFilter;
import com.algafoods.application.port.RelatorioVendasQueryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarVendasDiariasUseCase {

    private final RelatorioVendasQueryPort relatorioVendasQueryPort;

    public ConsultarVendasDiariasUseCase(RelatorioVendasQueryPort relatorioVendasQueryPort) {
        this.relatorioVendasQueryPort = relatorioVendasQueryPort;
    }

    public List<VendaDiariaDTO> execute(VendaDiariaFilter filter, String timeOffSet) {
        return relatorioVendasQueryPort.consultar(filter, timeOffSet);
    }
}
