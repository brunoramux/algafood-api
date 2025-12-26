package com.algafoods.infra.reports;

import com.algafoods.application.dto.VendaDiariaReportModel;
import com.algafoods.application.filter.VendaDiariaFilter;
import com.algafoods.application.port.VendasReportPort;
import com.algafoods.application.usecases.relatorio.ConsultarVendasDiariasUseCase;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

@Service
public class VendaReportPdfAdapter implements VendasReportPort {

    private final ConsultarVendasDiariasUseCase consultarVendasDiariasUseCase;

    public VendaReportPdfAdapter(ConsultarVendasDiariasUseCase consultarVendasDiariasUseCase) {
        this.consultarVendasDiariasUseCase = consultarVendasDiariasUseCase;
    }

    @Override
    public byte[] emitirVendasDiarias(VendaDiariaFilter filter, String timeOffSet) {

        try {

            InputStream jrxml = getClass().getResourceAsStream("/reports/vendaDiariaDTO.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);

            var parametros = new HashMap<String, Object>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

            var vendasDiarias = consultarVendasDiariasUseCase.execute(filter, timeOffSet)
                    .stream()
                    .map(v -> new VendaDiariaReportModel(
                    java.sql.Date.valueOf(v.getData()),
                    v.getTotalVendas(),
                    v.getTotalFaturado()
            )).toList();

            var dataSource = new JRBeanCollectionDataSource(vendasDiarias);

            var jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Problemas com geração do PDF", e);
        }

    }
}
