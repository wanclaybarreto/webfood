package com.wanclaybarreto.webfood.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	public Cliente findByEmail(String email);
	
	public Cliente findByCpf(String cpf);
	
}
