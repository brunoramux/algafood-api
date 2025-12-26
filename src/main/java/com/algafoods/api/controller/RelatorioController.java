package com.algafoods.api.controller;

import com.algafoods.application.dto.VendaDiariaDTO;
import com.algafoods.application.filter.VendaDiariaFilter;
import com.algafoods.application.usecases.relatorio.ConsultarVendasDiariasUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final ConsultarVendasDiariasUseCase consultarVendasDiariasUseCase;

    public RelatorioController(ConsultarVendasDiariasUseCase consultarVendasDiariasUseCase) {
        this.consultarVendasDiariasUseCase = consultarVendasDiariasUseCase;
    }

    @GetMapping("/vendas-diarias")
    public List<VendaDiariaDTO> consultar(
            @RequestParam(required = false, defaultValue = "+00:00") String timeOffSet,
            VendaDiariaFilter filtro
    ) {
        return consultarVendasDiariasUseCase.execute(filtro, timeOffSet);
    }
}
