package com.wanclaybarreto.webfood.domain.pagamento;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.wanclaybarreto.webfood.domain.pedido.Pedido;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pagamento implements Serializable {
	
	@EqualsAndHashCode.Include
	@Id
	private Integer id;
	
	@NotNull
	@OneToOne
	@MapsId
	private Pedido pedido;
	
	@NotNull
	@Column(name = "data_hora")
	private LocalDateTime dataHora;
	
	@NotNull
	@Column(name = "num_finais_cartao")
	@Size(min = 4, max = 4)
	private String numFinaisCartao;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private BandeiraCartao bandeiraCartao;
	
	
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
	
	
	
	public void defNumsFinaisEBand(String numCartao) {
		numFinaisCartao = numCartao.substring(12);
		bandeiraCartao = identificarBandeira(numCartao);
	}
	
	private BandeiraCartao identificarBandeira(String numCartao) {
		if (numCartao.startsWith("1111")) {
			return BandeiraCartao.AMEX;
		}
		
		if (numCartao.startsWith("2222")) {
			return BandeiraCartao.ELO;
		}
		
		if (numCartao.startsWith("3333")) {
			return BandeiraCartao.MASTER;
		}
		
		if (numCartao.startsWith("4444")) {
			return BandeiraCartao.VISA;
		}
		
		return BandeiraCartao.NOBAND;
	}
	
}
