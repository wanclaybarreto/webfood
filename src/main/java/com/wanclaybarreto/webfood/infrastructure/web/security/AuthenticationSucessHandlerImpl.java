package com.wanclaybarreto.webfood.infrastructure.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.wanclaybarreto.webfood.util.SecurityUtils;

public class AuthenticationSucessHandlerImpl implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Role role = SecurityUtils.loggedUser().getRole();
		
		if (role == Role.CLIENTE) {
			response.sendRedirect("cliente/home");
		} else if (role == Role.RESTAURANTE) {
			response.sendRedirect("restaurante/home");
		} else {
			throw new IllegalStateException("Erro de autenticação!");
		}
		
	}
	
}
