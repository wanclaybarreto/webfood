package com.wanclaybarreto.webfood.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wanclaybarreto.webfood.domain.cliente.Cliente;
import com.wanclaybarreto.webfood.domain.cliente.ClienteRepository;
import com.wanclaybarreto.webfood.domain.restaurante.Restaurante;
import com.wanclaybarreto.webfood.domain.restaurante.RestauranteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Transactional
	public void saveCliente(Cliente cliente) throws ValidationException {
		
		if ( !validateCpf(cliente.getCpf(), cliente.getId()) ) {
			throw new ValidationException("cpf--Esse CPF já pertence a outra conta.");
		}
		
		if ( !validateEmail(cliente.getEmail(), cliente.getId()) ) {
			throw new ValidationException("email--Esse e-mail já pertence a outra conta.");
		}
		
		if (cliente.getId() != null) {
			Cliente clienteDB = clienteRepository.findById(cliente.getId()).orElseThrow();
			cliente.setSenha(clienteDB.getSenha());
		} else {
			cliente.encryptPassword();
		}
		
		clienteRepository.save(cliente);
		
	}
	
	public boolean validateEmail(String email, Integer id) {
		Restaurante restaurante = restauranteRepository.findByEmail(email);
		if (restaurante != null) return false;
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if (cliente != null) {
			if (id == null) {
				return false;
			}
			
			if (!cliente.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean validateCpf(String cpf, Integer id) {
		Cliente cliente = clienteRepository.findByCpf(cpf);
		
		if (cliente != null) {
			if (id == null) {
				return false;
			}
			
			if (!cliente.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
}
