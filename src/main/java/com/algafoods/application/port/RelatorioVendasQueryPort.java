package com.algafoods.application.port;

import com.algafoods.application.dto.VendaDiariaDTO;
import com.algafoods.application.filter.VendaDiariaFilter;

import java.util.List;

public interface RelatorioVendasQueryPort {

    List<VendaDiariaDTO> consultar(VendaDiariaFilter filter, String timeOffSet);

}
