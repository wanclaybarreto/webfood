package com.wanclaybarreto.webfood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.wanclaybarreto.webfood.domain.pedido.Carrinho;
import com.wanclaybarreto.webfood.domain.pedido.ItemPedido;
import com.wanclaybarreto.webfood.domain.pedido.Pedido;
import com.wanclaybarreto.webfood.domain.pedido.PedidoRepository;
import com.wanclaybarreto.webfood.domain.pedido.RestauranteDiferenteException;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapio;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapioRepository;

@Controller
@RequestMapping("/cliente/carrinho")
@SessionAttributes("carrinho")
public class CarrinhoController {
	
	@Autowired
	ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@ModelAttribute("carrinho")
	public Carrinho carrinho() {
		return new Carrinho();
	}
	
	@GetMapping(path = "/visualizar")
	public String viewCarrinho() {
		return "cliente-carrinho";
	}
	
	@GetMapping(path = "/adicionar")
	public String addItem(Model model,
						  @RequestParam("itemCardapioId") Integer itemCardapioId,
						  @RequestParam("quantidade") Integer quantidade,
						  @RequestParam("observacoes") String observacoes,
						  @ModelAttribute("carrinho") Carrinho carrinho) {
		
		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemCardapioId).orElseThrow();
		
		try {
			
			carrinho.addItem(itemCardapio, quantidade, observacoes);
			
		} catch (RestauranteDiferenteException e) {
			
			e.printStackTrace();
			
			model.addAttribute("msg", "não é possível adicionar comidas de restaurantes diferentes em um mesmo pedido!");
		}
		
		return "cliente-carrinho";
		
	}
	
	@GetMapping(path = "/remover")
	public String delItem(Model model,
						  SessionStatus sessionStatus,
						  @RequestParam("itemCardapioId") Integer itemCardapioId,
						  @ModelAttribute("carrinho") Carrinho carrinho) {
		
		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemCardapioId).orElseThrow();
		
		carrinho.delItem(itemCardapio);
		
		if (carrinho.isEmpty()) {
			sessionStatus.setComplete();
		}
		
		return "cliente-carrinho";
		
	}
	
	@GetMapping(path = "/refazerCarrinho")
	public String refazerCarrinho(Model model,
								  @ModelAttribute("carrinho") Carrinho carrinho,
								  @RequestParam("pedidoId") Integer pedidoId) {
		
		Pedido pedido =  pedidoRepository.findById(pedidoId).orElseThrow();
		
		carrinho.clean();
		
		for (ItemPedido ip : pedido.getItens()) {
			carrinho.addItem(ip);
		}
		
		return "cliente-carrinho";
		
	}
	
}
