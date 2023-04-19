package com.wanclaybarreto.webfood.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wanclaybarreto.webfood.application.service.ClienteService;
import com.wanclaybarreto.webfood.application.service.RestauranteService;
import com.wanclaybarreto.webfood.application.service.ValidationException;
import com.wanclaybarreto.webfood.domain.cliente.Cliente;
import com.wanclaybarreto.webfood.domain.cliente.ClienteRepository;
import com.wanclaybarreto.webfood.domain.pedido.Pedido;
import com.wanclaybarreto.webfood.domain.pedido.PedidoRepository;
import com.wanclaybarreto.webfood.domain.restaurante.CategoriaRestauranteRepository;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapio;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapioRepository;
import com.wanclaybarreto.webfood.domain.restaurante.Restaurante;
import com.wanclaybarreto.webfood.domain.restaurante.RestauranteRepository;
import com.wanclaybarreto.webfood.domain.restaurante.SearchFilter;
import com.wanclaybarreto.webfood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@GetMapping("/home")
	public String home(Model model) {
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		
		model.addAttribute("searchFilter", new SearchFilter());
		
		List<Pedido> pedidos = pedidoRepository.findAllByCliente_IdOrderByDataHoraDesc(SecurityUtils.loggedCliente().getId());
		model.addAttribute("pedidos", pedidos);
		
		return "cliente-home";
	}
	
	@GetMapping("/edit")
	public String edit(Model model) {
		Integer clienteId = SecurityUtils.loggedCliente().getId();
		Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();
		model.addAttribute("cliente", cliente);
		ControllerHelper.setEditMode(model, true);
		
		return "cliente-cadastro";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {
		
		if (!errors.hasErrors()) {
			
			try {
				clienteService.saveCliente(cliente);
				model.addAttribute("msg", "Dados alterados com sucesso!");
			} catch (ValidationException e) {
				String[] fieldAndMessage = e.getMessage().split("--"); 
				errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
			}
			
		}
		
		System.out.println(errors);
		
		ControllerHelper.setEditMode(model, true);
		
		return "cliente-cadastro";
		
	}
	
	@GetMapping(path = "/search")
	public String search(@ModelAttribute("searchFilter") SearchFilter filter, @RequestParam(value = "cmd", required = false) String cmd, Model model) {
		
		filter.processFilter(cmd);
		
		List<Restaurante> restaurantes = restauranteService.search(filter);
		model.addAttribute("restaurantes", restaurantes);
		
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		
		model.addAttribute("searchFilter", filter);
		
		return "cliente-busca";
		
	}
	
	@GetMapping(path = "/restaurante")
	public String viewRestaurante(@RequestParam("restauranteId") Integer restauranteId,
								  @RequestParam(value = "categoria", required = false) String categoria,
								  Model model) {
		
		Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
		model.addAttribute("restaurante", restaurante);
		
		List<String> categorias = itemCardapioRepository.findCategorias(restauranteId);
		model.addAttribute("categorias", categorias);
		
		List<ItemCardapio> itensCardapioDestaque;
		List<ItemCardapio> itensCardapioSemDestaque;
		
		if (categoria == null) {
			
			itensCardapioDestaque = itemCardapioRepository.findAllByRestaurante_IdAndDestaqueOrderByNome(restauranteId, true);
			itensCardapioSemDestaque = itemCardapioRepository.findAllByRestaurante_IdAndDestaqueOrderByNome(restauranteId, false);
			
			model.addAttribute("withFilterCategorias", false);
			
		} else {
			
			itensCardapioDestaque = itemCardapioRepository.findAllByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria);
			itensCardapioSemDestaque = itemCardapioRepository.findAllByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria);
			
			model.addAttribute("withFilterCategorias", true);
			
		}
		
		model.addAttribute("categoriaSelected", categoria);
		
		model.addAttribute("itensCardapioDestaque", itensCardapioDestaque);
		model.addAttribute("itensCardapioSemDestaque", itensCardapioSemDestaque);
		
		return "cliente-restaurante";
		
	}
	
}
