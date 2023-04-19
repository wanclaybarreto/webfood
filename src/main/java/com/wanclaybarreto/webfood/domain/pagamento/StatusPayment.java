package com.wanclaybarreto.webfood.domain.pagamento;

public enum StatusPayment {
	
	Authorized("Autorizado!"),
	NotAuthorized("não autorizado pela instituição financeira em questão!"),
	InvalidCard("Cartão inválido ou bloqueado!");
	
	String description;
	
	private StatusPayment(String descricao) {
		this.description = descricao;
	}
	
	public String getDescricao() {
		return description;
	}
	
}
