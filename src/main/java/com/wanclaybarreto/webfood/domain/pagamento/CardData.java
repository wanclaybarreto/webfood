package com.wanclaybarreto.webfood.domain.pagamento;

import javax.validation.constraints.Pattern;

public class CardData {
	
	@Pattern(regexp = "\\d{16}", message = "O número do cartão é inválido!")
	private String number;
	
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
