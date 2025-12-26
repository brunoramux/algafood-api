package com.algafoods.application.port;

import com.algafoods.application.filter.VendaDiariaFilter;
import net.sf.jasperreports.engine.JRException;

public interface VendasReportPort {

    byte[] emitirVendasDiarias(VendaDiariaFilter filter, String timeOffSet) throws JRException;

}
