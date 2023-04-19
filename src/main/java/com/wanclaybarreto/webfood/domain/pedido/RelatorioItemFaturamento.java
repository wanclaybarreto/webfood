package com.wanclaybarreto.webfood.domain.pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RelatorioItemFaturamento {

    private String nome;
    private Long quantidade;
    private BigDecimal faturamento;

}
