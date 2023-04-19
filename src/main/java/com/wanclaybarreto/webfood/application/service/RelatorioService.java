package com.wanclaybarreto.webfood.application.service;

import com.wanclaybarreto.webfood.domain.pedido.*;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapio;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapioRepository;
import com.wanclaybarreto.webfood.domain.restaurante.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ItemCardapioRepository itemCardapioRepository;

    @Autowired
    RestauranteRepository restauranteRepository;

    //TODO Refatorar este método para que o mesmo passe a utilizar LocalDateTime.MIN e LocalDateTime.MAX, reduzindo
    //TODO a quantidade de IFs e de métodos no repositório.
    public List<Pedido> listarPedidos(Integer restauranteId, RelatorioPedidoFilter filter) {
        if (filter.getPedidoId() != null && filter.getDataInicial() == null && filter.getDataFinal() == null) {
            //Busca por pedido
            Pedido pedido = pedidoRepository.findByIdAndRestaurante_Id(filter.getPedidoId(), restauranteId);

            if (pedido == null) {
                return new ArrayList<Pedido>(0);
            }

            return List.of(pedido);

        } else if (filter.getPedidoId() == null && filter.getDataInicial() != null && filter.getDataFinal() == null) {
            //Busca por data inicial
            return pedidoRepository.findAllByRestauranteAndDataInicialOrderByDataHoraDesc(
                    restauranteId, filter.getDataInicial().atStartOfDay()
            );

        } else if (filter.getPedidoId() == null && filter.getDataInicial() == null && filter.getDataFinal() != null) {
            //Busca por data final
            return pedidoRepository.findAllByRestauranteAndDataFinalOrderByDataHoraDesc(
                    restauranteId, filter.getDataFinal().atTime(23, 59, 59)
            );

        } else if (filter.getPedidoId() != null && filter.getDataInicial() != null && filter.getDataFinal() == null) {
            //Busca por pedido e data inicial
            Pedido pedido = pedidoRepository.findByIdAndRestauranteAndDataInicial(
                    filter.getPedidoId(), restauranteId, filter.getDataInicial().atStartOfDay()
            );

            if (pedido == null) {
                return new ArrayList<Pedido>(0);
            }

            return List.of(pedido);

        } else if (filter.getPedidoId() != null && filter.getDataInicial() == null && filter.getDataFinal() != null) {
            //Busca por pedido e data final
            Pedido pedido = pedidoRepository.findByIdAndRestauranteAndDataFinal(
                    filter.getPedidoId(), restauranteId, filter.getDataFinal().atTime(23, 59, 59)
            );

            if (pedido == null) {
                return new ArrayList<Pedido>(0);
            }

            return List.of(pedido);

        } else if (filter.getPedidoId() == null && filter.getDataInicial() != null && filter.getDataFinal() != null) {
            //Busca por data final e data inicial
            return pedidoRepository.findAllByRestauranteAndDataInicialAndDataFinalOrderByDataHoraDesc(
                    restauranteId,
                    filter.getDataInicial().atStartOfDay(),
                    filter.getDataFinal().atTime(23, 59, 59)
            );

        }

        return pedidoRepository.findAllByRestaurante_IdOrderByDataHoraDesc(restauranteId);
    }

    public List<RelatorioItemFaturamento> calcularFaturamentoItens(Integer restauranteId, RelatorioItemFilter filter) {
        List<RelatorioItemFaturamento> itens = new ArrayList<>();
        List<Object[]> itensObj;

        LocalDateTime dataInicial = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
        if (filter.getDataInicial() != null) {
            dataInicial = filter.getDataInicial().atStartOfDay();
        }

        LocalDateTime dataFinal = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        if (filter.getDataFinal() != null) {
            dataFinal = filter.getDataFinal().atTime(23, 59, 59);
        }

        if (filter.getItemId() != null) {
            itensObj = itemCardapioRepository.findItensFaturamento(
                    restauranteId, filter.getItemId(), dataInicial, dataFinal
            );
        } else {
            itensObj = itemCardapioRepository.findItensFaturamento(restauranteId, dataInicial, dataFinal);
        }

        for (Object[] item : itensObj) {
            itens.add(
                    new RelatorioItemFaturamento(
                            (String) item[0],
                            (Long) item[1],
                            (BigDecimal) item[2]
                    )
            );
        }

        return itens;
    }

}
