package com.wanclaybarreto.webfood.application.service;

@SuppressWarnings("serial")
public class PagamentoException extends Exception {
	
	public PagamentoException() {}
	
	public PagamentoException(String message) {
		super(message);
	}
	
	PagamentoException(Throwable cause) {
		super(cause);
	}
	
	PagamentoException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
