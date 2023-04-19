package com.wanclaybarreto.webfood.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.wanclaybarreto.webfood.domain.cliente.Cliente;
import com.wanclaybarreto.webfood.domain.restaurante.Restaurante;
import com.wanclaybarreto.webfood.infrastructure.web.security.LoggedUser;

public class SecurityUtils {
	
	public static LoggedUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		return (LoggedUser) authentication.getPrincipal();
	}
	
	public static Cliente loggedCliente() {
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			throw new IllegalStateException("não existe um usuário logado!");
		}
		
		if (!(loggedUser.getUsuario() instanceof Cliente)) {
			throw new IllegalStateException("O usuário logado não é um cliente!");
		}
		
		return (Cliente) loggedUser.getUsuario();
	}
	
	public static Restaurante loggedRestaurante() {
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			throw new IllegalStateException("não existe um usuário logado!");
		}
		
		if (!(loggedUser.getUsuario() instanceof Restaurante)) {
			throw new IllegalStateException("O usuário logado não é um restaurante!");
		}
		
		return (Restaurante) loggedUser.getUsuario();
	}
	
}
