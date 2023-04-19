package com.wanclaybarreto.webfood.application.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.wanclaybarreto.webfood.domain.pagamento.CardData;
import com.wanclaybarreto.webfood.domain.pagamento.Pagamento;
import com.wanclaybarreto.webfood.domain.pagamento.PagamentoRepository;
import com.wanclaybarreto.webfood.domain.pagamento.StatusPayment;
import com.wanclaybarreto.webfood.domain.pedido.Carrinho;
import com.wanclaybarreto.webfood.domain.pedido.ItemPedido;
import com.wanclaybarreto.webfood.domain.pedido.ItemPedidoPK;
import com.wanclaybarreto.webfood.domain.pedido.ItemPedidoRepository;
import com.wanclaybarreto.webfood.domain.pedido.Pedido;
import com.wanclaybarreto.webfood.domain.pedido.Pedido.StatusPedido;
import com.wanclaybarreto.webfood.domain.pedido.PedidoRepository;
import com.wanclaybarreto.webfood.util.SecurityUtils;

@Service
public class PedidoService {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	PagamentoRepository pagamentoRepository;
	
	
	
	@Value("${webfood.payapi.url}")
	private String payapiUrl;
	
	@Value("${webfood.payapi.token}")
	private String payapiToken;
	
	
	
	//Criar pedido, processar pagamento e retornar pedido recém criado
	@Transactional(rollbackFor = PagamentoException.class)
	public Pedido criarEPagar(Carrinho carrinho, String numCartaoCredito) throws PagamentoException {
		
		Pedido pedido = new Pedido();
		pedido.setStatus(StatusPedido.Producao);
		pedido.setDataHora(LocalDateTime.now());
		pedido.setCliente(SecurityUtils.loggedCliente());
		pedido.setRestaurante(carrinho.getRestaurante());
		pedido.setTaxaEntrega(carrinho.getRestaurante().getTaxaEntrega());
		pedido.setSubtotal(carrinho.calcPrecoTotal(false));
		pedido.setTotal(carrinho.calcPrecoTotal(true));
		
		pedido = pedidoRepository.save(pedido);
		
		int ordem = 1;
		
		for (ItemPedido ip : carrinho.getItens()) {
			
			ip.setId(new ItemPedidoPK(pedido, ordem++));
			itemPedidoRepository.save(ip);
			
		}
		
		CardData cardData = new CardData();
		cardData.setNumber(numCartaoCredito);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Token", payapiToken);
		
		HttpEntity<CardData> requestEntity = new HttpEntity<>(cardData, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		Map<String, String> response;
		
		try {
			response = restTemplate.postForObject(payapiUrl, requestEntity, Map.class);
		} catch (Exception e) {
			throw new PagamentoException("Erro no servidor de pagamento!");
		}
		
		StatusPayment statusPagamento = StatusPayment.valueOf(response.get("status"));
		
		if (statusPagamento != StatusPayment.Authorized) {
			throw new PagamentoException(statusPagamento.getDescricao());
		}
		
		Pagamento pagamento = new Pagamento();
		pagamento.setDataHora(LocalDateTime.now());
		pagamento.setPedido(pedido);
		pagamento.defNumsFinaisEBand(numCartaoCredito);
		
		pagamentoRepository.save(pagamento);
		
		return pedido;
	}
	
}
