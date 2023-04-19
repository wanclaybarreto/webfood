package com.wanclaybarreto.webfood.domain.pedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.wanclaybarreto.webfood.domain.restaurante.ItemCardapio;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "item_pedido")
public class ItemPedido implements Serializable {
	
	@EqualsAndHashCode.Include
	@EmbeddedId
	private ItemPedidoPK id;
	
	@NotNull
	@ManyToOne
	private ItemCardapio itemCardapio;
	
	@NotNull
	private Integer quantidade;
	
	private String observacoes;
	
	@NotNull
	private BigDecimal preco;
	
	public BigDecimal calcPrecoTotal() {
		return preco.multiply(BigDecimal.valueOf(quantidade.longValue())).setScale(2, RoundingMode.HALF_UP);
	}
	
}
