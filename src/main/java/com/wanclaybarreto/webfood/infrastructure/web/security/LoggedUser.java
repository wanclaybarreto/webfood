package com.wanclaybarreto.webfood.infrastructure.web.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wanclaybarreto.webfood.domain.cliente.Cliente;
import com.wanclaybarreto.webfood.domain.restaurante.Restaurante;
import com.wanclaybarreto.webfood.domain.usuario.Usuario;

@SuppressWarnings("serial")
public class LoggedUser implements UserDetails {
	
	private Usuario usuario;
	private Role role;
	private Collection<? extends GrantedAuthority> roles;
	
	public LoggedUser(Usuario usuario) {
		this.usuario = usuario;
		
		if (usuario instanceof Cliente) {
			this.role = Role.CLIENTE;
		} else if (usuario instanceof Restaurante) {
			this.role = Role.RESTAURANTE;
		} else {
			throw new IllegalStateException("O tipo de usuário não é válido!");
		}
		
		this.roles = List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public Role getRole() {
		return role;
	}
	
}
