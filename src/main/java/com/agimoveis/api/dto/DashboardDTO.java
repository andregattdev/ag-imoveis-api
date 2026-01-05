package com.agimoveis.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private Long totalImoveis;
    private Long totalAtivos;
    private Long totalInativos;
    private Map<String, Long> porTipo; // Ex: CASA: 10, APARTAMENTO: 5
    private Map<String, Long> porFinalidade; // Ex: VENDA: 8, LOCACAO: 7
}