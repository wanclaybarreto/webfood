package com.wanclaybarreto.webfood.domain.restaurante;

import java.util.Comparator;

import com.wanclaybarreto.webfood.domain.restaurante.SearchFilter.Order;

public class RestauranteComparator implements Comparator<Restaurante> {
	
	private SearchFilter filter;
	private String cep;
	
	public RestauranteComparator(SearchFilter filter, String cep) {
		this.filter = filter;
		this.cep = cep;
	}
	
	@Override
	public int compare(Restaurante r1, Restaurante r2) {
		
		int result;
		
		if (filter.getOrder() == Order.Taxa) {
			
			result = r1.getTaxaEntrega().compareTo(r2.getTaxaEntrega());
			
		} else if (filter.getOrder() == Order.Tempo) {
			
			//TODO Utilizar método de cálculo de tempo de entrega 
			
			result = r1.getTempoEntregaBase().compareTo(r2.getTempoEntregaBase());
			
		} else {
			throw new IllegalStateException("O valor de ordenação - " + filter.getOrder() + " - não é válido!");
		}
		
		return filter.isAsc() ? result : -result;
		
	}

}
