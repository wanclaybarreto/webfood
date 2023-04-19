package com.wanclaybarreto.webfood.domain.pedido;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RelatorioItemFilter {

    private Integer itemId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicial;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFinal;

    public boolean isDataFinalLessThanDataInicial() {
        if (dataInicial != null && dataFinal != null){
            return dataFinal.isBefore(dataInicial);
        }

        return false;
    }

}
