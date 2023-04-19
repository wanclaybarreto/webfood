package com.wanclaybarreto.webfood.infrastructure.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping(path = {"/", "/login"})
	public String login() {
		
//		if (SecurityUtils.loggedUser() != null) {
//			try {
//				if (SecurityUtils.loggedCliente() != null) {
//					return "cliente-home";
//				}
//			} catch (IllegalStateException e) {
//				if (SecurityUtils.loggedRestaurante() != null) {
//					return "restaurante-home";
//				}
//			}
//		}
		
		return "login";
	}
	
	@GetMapping(path = {"/login-error"})
	public String loginError(Model model) {
		model.addAttribute("msg", "Credenciais inválidas!");
		return "login";
	}
	
}
