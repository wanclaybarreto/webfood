package com.wanclaybarreto.webfood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.wanclaybarreto.webfood.application.service.PagamentoException;
import com.wanclaybarreto.webfood.application.service.PedidoService;
import com.wanclaybarreto.webfood.domain.pedido.Carrinho;
import com.wanclaybarreto.webfood.domain.pedido.Pedido;

@Controller
@RequestMapping("/cliente/pagamento")
@SessionAttributes("carrinho")
public class PagamentoController {
	
	@Autowired
	private PedidoService pedidoService;
	
	@PostMapping(path = "/pagar")
	public String pagar(@RequestParam("numCartaoCredito") String numCartaoCredito, @ModelAttribute("carrinho") Carrinho carrinho,
						SessionStatus sessionSatatus, Model model) {
		
		Pedido pedido;
		try {
			
			pedido = pedidoService.criarEPagar(carrinho, numCartaoCredito);
			
			sessionSatatus.setComplete();
			
			return "redirect:/cliente/pedido/view?pedidoId=" + pedido.getId();
			
		} catch (PagamentoException e) {
			
			model.addAttribute("msg", e.getMessage());
			
			return "cliente-carrinho";
			
		}
		
	}
	
}
