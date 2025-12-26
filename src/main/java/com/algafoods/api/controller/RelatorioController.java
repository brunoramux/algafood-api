package com.algafoods.api.controller;

import com.algafoods.application.dto.VendaDiariaDTO;
import com.algafoods.application.filter.VendaDiariaFilter;
import com.algafoods.application.port.VendasReportPort;
import com.algafoods.application.usecases.relatorio.ConsultarVendasDiariasUseCase;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final ConsultarVendasDiariasUseCase consultarVendasDiariasUseCase;
    private final VendasReportPort vendasReportPort;

    public RelatorioController(ConsultarVendasDiariasUseCase consultarVendasDiariasUseCase, VendasReportPort vendasReportPort) {
        this.consultarVendasDiariasUseCase = consultarVendasDiariasUseCase;
        this.vendasReportPort = vendasReportPort;
    }

    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiariaDTO> consultar(
            @RequestParam(required = false, defaultValue = "+00:00") String timeOffSet,
            VendaDiariaFilter filtro
    ) {
        return consultarVendasDiariasUseCase.execute(filtro, timeOffSet);
    }

    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarPdf(
            @RequestParam(required = false, defaultValue = "+00:00") String timeOffSet,
            VendaDiariaFilter filtro
    ) throws JRException {
        byte[] pdf = vendasReportPort.emitirVendasDiarias(filtro, timeOffSet);

        var headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(pdf);
    }
}
