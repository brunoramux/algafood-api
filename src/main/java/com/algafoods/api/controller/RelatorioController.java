package com.algafoods.api.controller;

import com.algafoods.application.dto.VendaDiariaDTO;
import com.algafoods.application.filter.VendaDiariaFilter;
import com.algafoods.application.usecases.relatorio.ConsultarVendasDiariasUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final ConsultarVendasDiariasUseCase useCase;


    public RelatorioController(ConsultarVendasDiariasUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/vendas-diarias")
    public List<VendaDiariaDTO> consultar(VendaDiariaFilter filtro) {
        return useCase.execute(filtro);
    }
}
