package com.wanclaybarreto.webfood.domain.pedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.wanclaybarreto.webfood.domain.cliente.Cliente;
import com.wanclaybarreto.webfood.domain.pagamento.Pagamento;
import com.wanclaybarreto.webfood.domain.restaurante.Restaurante;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido implements Serializable {
	
	public enum StatusPedido {
		Producao(1, "Em  produção!", false),
		Entrega(2, "Saiu para entrega!", false),
		Concluido(3, "Concluído!", true);
		
		StatusPedido(int ordem, String descricao, boolean ultimo) {
			this.ordem = ordem;
			this.descricao = descricao;
			this.ultimo = ultimo;
		}
		
		int ordem;
		String descricao;
		boolean ultimo;

		public static StatusPedido fromOrdem(int ordem) {
			for (StatusPedido status : StatusPedido.values()) {
				if (status.getOrdem() == ordem) {
					return status;
				}
			}
			return null;
		}

		public int getOrdem() {
			return ordem;
		}
		
		public String getDescricao() {
			return descricao;
		}
		
		public boolean isUltimo() {
			return ultimo;
		}
	}
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private StatusPedido status;
	
	@NotNull
	@Column(name = "data_hora")
	private LocalDateTime dataHora;
	
	@NotNull
	@ManyToOne
	private Cliente cliente;
	
	@NotNull
	@ManyToOne
	private Restaurante restaurante;
	
	
	@OneToMany(mappedBy = "id.pedido", fetch = FetchType.EAGER)
	private Set<ItemPedido> itens;
	
	@NotNull
	@Column(name = "taxa_entrega")
	private BigDecimal taxaEntrega;
	
	@NotNull
	private BigDecimal subtotal;
	
	@NotNull
	private BigDecimal total;
	
	@OneToOne(mappedBy = "pedido")
	private Pagamento pagamento;
	
	
	public String getFormattedId() {
		return String.format("#%04d", id);
	}

	public void definirProximoStatus() {
		StatusPedido novoStatus = null;

		for (StatusPedido status : StatusPedido.values()) {
			if (status.getOrdem() == this.status.getOrdem()) {
				novoStatus = StatusPedido.fromOrdem(status.getOrdem() + 1);
			}
		}

		if (novoStatus != null) {
			this.status = novoStatus;
		}
	}
	
}
