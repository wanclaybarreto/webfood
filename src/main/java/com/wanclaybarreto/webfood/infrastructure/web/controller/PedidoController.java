package com.wanclaybarreto.webfood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wanclaybarreto.webfood.domain.pedido.Pedido;
import com.wanclaybarreto.webfood.domain.pedido.PedidoRepository;

@Controller
@RequestMapping("/cliente/pedido")
public class PedidoController {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@GetMapping(path = "/view")
	public String viewPedido(Model model, @RequestParam("pedidoId") Integer pedidoId) {
		
		Pedido pedido =  pedidoRepository.findById(pedidoId).orElseThrow();
		model.addAttribute("pedido", pedido);
		
		return "cliente-pedido";
		
	}
	
}
