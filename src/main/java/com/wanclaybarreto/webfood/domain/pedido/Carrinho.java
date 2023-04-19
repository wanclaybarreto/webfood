package com.wanclaybarreto.webfood.domain.pedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapio;
import com.wanclaybarreto.webfood.domain.restaurante.Restaurante;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class Carrinho implements Serializable {
	
	private List<ItemPedido> itens = new ArrayList<>();
	private Restaurante restaurante;
	
	public void addItem(ItemCardapio itemCardapio, Integer quantidade, String observacoes) throws RestauranteDiferenteException {
		
		if (itens.size() == 0) {
			restaurante = itemCardapio.getRestaurante();
		} else if (!itemCardapio.getRestaurante().getId().equals(restaurante.getId())) {
			throw new RestauranteDiferenteException();
		}
		
		if (!exists(itemCardapio)) {
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setItemCardapio(itemCardapio);
			itemPedido.setQuantidade(quantidade);
			itemPedido.setObservacoes(observacoes);
			itemPedido.setPreco(itemCardapio.getPreco());
			
			itens.add(itemPedido);
		}
		
		//TODO Realizar alguma ação (retorno de mensagem ou algo do tipo) para adição de itens repetidos
		
	}
	
	public void addItem(ItemPedido itemPedido) {
		try {
			
			addItem(itemPedido.getItemCardapio(), itemPedido.getQuantidade(), itemPedido.getObservacoes());
			
		} catch (RestauranteDiferenteException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public void delItem(ItemCardapio itemCardapio) {
		for (Iterator<ItemPedido> iterator = itens.iterator(); iterator.hasNext();) {
			ItemPedido itemPedido = iterator.next();
			
			if (itemPedido.getItemCardapio().getId().equals(itemCardapio.getId())) {
				iterator.remove();
				break;
			}
		}
		
		if (itens.size() == 0) {
			restaurante = null;
		}
	}
	
	private boolean exists(ItemCardapio itemCardapio) {
		for (ItemPedido ip : itens) {
			if (ip.getItemCardapio().getId().equals(itemCardapio.getId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public BigDecimal calcPrecoTotal(boolean addTaxaEntrega) {
		
		BigDecimal precoTotal = BigDecimal.ZERO;
		
		for (ItemPedido ip : itens) {
			precoTotal = precoTotal.add(ip.calcPrecoTotal());
		}
		
		if (addTaxaEntrega) precoTotal = precoTotal.add(restaurante.getTaxaEntrega());
		
		precoTotal = precoTotal.setScale(2, RoundingMode.HALF_UP);
		
		return precoTotal;
		
	}
	
	public void clean() {
		itens.clear();
		restaurante = null;
	}
	
	public boolean isEmpty() {
		return itens.size() == 0;
	}
	
}
