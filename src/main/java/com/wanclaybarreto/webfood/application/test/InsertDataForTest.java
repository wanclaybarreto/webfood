package com.wanclaybarreto.webfood.application.test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.wanclaybarreto.webfood.domain.pedido.Pedido;
import com.wanclaybarreto.webfood.domain.pedido.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.wanclaybarreto.webfood.domain.cliente.Cliente;
import com.wanclaybarreto.webfood.domain.cliente.ClienteRepository;
import com.wanclaybarreto.webfood.domain.restaurante.CategoriaRestaurante;
import com.wanclaybarreto.webfood.domain.restaurante.CategoriaRestauranteRepository;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapio;
import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapioRepository;
import com.wanclaybarreto.webfood.domain.restaurante.Restaurante;
import com.wanclaybarreto.webfood.domain.restaurante.RestauranteRepository;
import com.wanclaybarreto.webfood.util.StringUtils;

@Component
public class InsertDataForTest {
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	RestauranteRepository restauranteRepository;
	
	@Autowired
	CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	ItemCardapioRepository itemCardapioRepository;

	@Autowired
	PedidoRepository pedidoRepository;
	
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		Cliente[] clientes = clientes();
		Restaurante[] restaurantes = restaurantes();
		itensCardapio(restaurantes);

		Pedido p1 = new Pedido();
		p1.setDataHora(LocalDateTime.now());
		p1.setCliente(clientes[0]);
		p1.setRestaurante(restaurantes[0]);
		p1.setStatus(Pedido.StatusPedido.Producao);
		p1.setTaxaEntrega(BigDecimal.valueOf(2));
		p1.setSubtotal(BigDecimal.valueOf(10));
		p1.setTotal(BigDecimal.valueOf(12));
		pedidoRepository.save(p1);
		
	}
	
	private Restaurante[] restaurantes() {
		List<Restaurante> restaurantes = new ArrayList<>();
		
		CategoriaRestaurante categoriaPizza = categoriaRestauranteRepository.findById(1).orElseThrow(NoSuchElementException::new);
		CategoriaRestaurante categoriaSanduiche = categoriaRestauranteRepository.findById(2).orElseThrow(NoSuchElementException::new);
		CategoriaRestaurante categoriaSalada = categoriaRestauranteRepository.findById(4).orElseThrow(NoSuchElementException::new);
		CategoriaRestaurante categoriaSobremesa = categoriaRestauranteRepository.findById(5).orElseThrow(NoSuchElementException::new);
		
		Restaurante r = new Restaurante();
		r.setNome("Bubger King");
		r.setEmail("bk@email.com.br");
		r.setSenha(StringUtils.encrypt("bk123456"));
		r.setCep("40400400");
		r.setCnpj("00000000000101");
		r.setTaxaEntrega(BigDecimal.valueOf(3.2));
		r.setTelefone("99876671010");
		r.getCategorias().add(categoriaSanduiche);
		r.getCategorias().add(categoriaSalada);
		r.setLogotipo("1-logo.png");
		r.setTempoEntregaBase(30);
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setNome("Mc Naldo's");
		r.setEmail("mn@email.com.br");
		r.setSenha(StringUtils.encrypt("mn123456"));
		r.setCep("40400400");
		r.setCnpj("00000000000102");
		r.setTaxaEntrega(BigDecimal.valueOf(4.5));
		r.setTelefone("99876671011");
		r.getCategorias().add(categoriaSanduiche);
		r.getCategorias().add(categoriaSalada);
		r.setLogotipo("2-logo.png");
		r.setTempoEntregaBase(25);
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setNome("Sbubby");
		r.setEmail("s@email.com.br");
		r.setSenha(StringUtils.encrypt("s123456"));
		r.setCep("40400400");
		r.setCnpj("00000000000103");
		r.setTaxaEntrega(BigDecimal.valueOf(12.2));
		r.setTelefone("99876671012");
		r.getCategorias().add(categoriaSanduiche);
		r.getCategorias().add(categoriaSalada);
		r.setLogotipo("3-logo.png");
		r.setTempoEntregaBase(38);
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setNome("Pizza Brut");
		r.setEmail("pb@email.com.br");
		r.setSenha(StringUtils.encrypt("pb123456"));
		r.setCep("40400400");
		r.setCnpj("00000000000104");
		r.setTaxaEntrega(BigDecimal.valueOf(9.8));
		r.setTelefone("99876671013");
		r.getCategorias().add(categoriaPizza);
		r.getCategorias().add(categoriaSalada);
		r.setLogotipo("4-logo.png");
		r.setTempoEntregaBase(22);
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setNome("Wiki Japa");
		r.setEmail("wj@email.com.br");
		r.setSenha(StringUtils.encrypt("wj123456"));
		r.setCep("40400400");
		r.setCnpj("00000000000105");
		r.setTaxaEntrega(BigDecimal.valueOf(14.9));
		r.setTelefone("99876671014");
		r.getCategorias().add(categoriaSobremesa);
		r.getCategorias().add(categoriaSalada);
		r.setLogotipo("5-logo.png");
		r.setTempoEntregaBase(19);
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		Restaurante[] array = new Restaurante[restaurantes.size()]; 
		return restaurantes.toArray(array);
	}
	
	private Cliente[] clientes() {
		List<Cliente> clientes = new ArrayList<>(); 
		
		Cliente c = new Cliente();
		c.setNome("João Silva");
		c.setEmail("joao@email.com.br");
		c.setSenha(StringUtils.encrypt("j123456"));
		c.setCep("89300100");
		c.setCpf("03099887234");
		c.setTelefone("99355430001");
		clientes.add(c);
		clienteRepository.save(c);
		
		c = new Cliente();
		c.setNome("Maria Torres");
		c.setEmail("mariatorres@email.com.br");
		c.setSenha(StringUtils.encrypt("mt123456"));
		c.setCep("89300101");
		c.setCpf("03099887677");
		c.setTelefone("99355430002");
		clientes.add(c);
		clienteRepository.save(c);
		
		Cliente[] array = new Cliente[clientes.size()]; 
		return clientes.toArray(array);
	}
	
	private void itensCardapio(Restaurante[] restaurantes) {
		ItemCardapio ic = new ItemCardapio();
		ic.setCategoria("Sanduíche");
		ic.setDescricao("Delicioso Sanduíche com molho.");
		ic.setNome("Double Cheese Burger Special");
		ic.setPreco(BigDecimal.valueOf(23.8));
		ic.setRestaurante(restaurantes[0]);
		ic.setDestaque(true);
		ic.setImagem("1-itemcardapio.png");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria("Sanduíche");
		ic.setDescricao("Sanduíche padrão que mata a fome.");
		ic.setNome("Cheese Burger Simples");
		ic.setPreco(BigDecimal.valueOf(17.8));
		ic.setRestaurante(restaurantes[0]);
		ic.setDestaque(false);
		ic.setImagem("2-itemcardapio.png");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria("Sanduíche");
		ic.setDescricao("Sanduíche natural com peito de peru.");
		ic.setNome("Sanduíche Natural da Casa");
		ic.setPreco(BigDecimal.valueOf(11.8));
		ic.setRestaurante(restaurantes[0]);
		ic.setDestaque(false);
		ic.setImagem("3-itemcardapio.png");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria("Bebida");
		ic.setDescricao("Refrigerante com gás.");
		ic.setNome("Refrigerante Tradicional");
		ic.setPreco(BigDecimal.valueOf(9));
		ic.setRestaurante(restaurantes[0]);
		ic.setDestaque(false);
		ic.setImagem("4-itemcardapio.png");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria("Bebida");
		ic.setDescricao("Suco natural da fruta.");
		ic.setNome("Suco de Laranja");
		ic.setPreco(BigDecimal.valueOf(9));
		ic.setRestaurante(restaurantes[0]);
		ic.setDestaque(false);
		ic.setImagem("5-itemcardapio.png");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria("Pizza");
		ic.setDescricao("Pizza saborosa com cebola.");
		ic.setNome("Pizza de Calabresa");
		ic.setPreco(BigDecimal.valueOf(38.9));
		ic.setRestaurante(restaurantes[3]);
		ic.setDestaque(false);
		ic.setImagem("6-itemcardapio.png");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria("Japonesa");
		ic.setDescricao("Delicioso Uramaki tradicional.");
		ic.setNome("Uramaki");
		ic.setPreco(BigDecimal.valueOf(16.8));
		ic.setRestaurante(restaurantes[4]);
		ic.setDestaque(false);
		ic.setImagem("7-itemcardapio.png");
		itemCardapioRepository.save(ic);
	}
	
}
