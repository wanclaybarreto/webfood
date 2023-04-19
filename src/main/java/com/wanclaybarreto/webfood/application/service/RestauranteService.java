package com.wanclaybarreto.webfood.application.service;

import java.util.Iterator;
import java.util.List;

import com.wanclaybarreto.webfood.domain.restaurante.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wanclaybarreto.webfood.domain.restaurante.SearchFilter.SearchType;
import com.wanclaybarreto.webfood.util.SecurityUtils;
import com.wanclaybarreto.webfood.domain.cliente.Cliente;
import com.wanclaybarreto.webfood.domain.cliente.ClienteRepository;

@Service
public class RestauranteService {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional
	public void saveRestaurante(Restaurante restaurante) throws ValidationException {
		
		if ( !validateCnpj(restaurante.getCnpj(), restaurante.getId()) ) {
			throw new ValidationException("cnpj--Esse CNPJ já pertence a outra conta.");
		}
		
		if ( !validateEmail(restaurante.getEmail(), restaurante.getId()) ) {
			throw new ValidationException("email--Esse e-mail já pertence a outra conta.");
		}
		
		if (restaurante.getId() != null) {
			Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow();
			restaurante.setSenha(restauranteDB.getSenha());
			restaurante.setLogotipo(restauranteDB.getLogotipo());
			restauranteRepository.save(restaurante);
		} else {
			restaurante.encryptPassword();
			restaurante = restauranteRepository.save(restaurante);
			restaurante.setLogotipoFileName();
			imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
		}
		
	}
	
	public boolean validateEmail(String email, Integer id) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente != null) return false;
		
		Restaurante restaurante = restauranteRepository.findByEmail(email);
		
		if (restaurante != null) {
			if (id == null) {
				return false;
			}
			
			if (!restaurante.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean validateCnpj(String cnpj, Integer id) {
		Restaurante restaurante = restauranteRepository.findByCnpj(cnpj);
		
		if (restaurante != null) {
			if (id == null) {
				return false;
			}
			
			if (!restaurante.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
	public List<Restaurante> search(SearchFilter filter) {
		
		List<Restaurante> restaurantes;
		
		if (filter.getSearchType() == SearchType.Texto) {
			
			restaurantes = restauranteRepository.findAllByNomeIgnoreCaseContaining(filter.getTexto());
			
		} else if (filter.getSearchType() == SearchType.Categoria) {
			
			restaurantes = restauranteRepository.findAllByCategorias_Id(filter.getCategoriaId());
			
		} else {
			
			throw new IllegalStateException("O tipo de busca (" + filter.getSearchType() + ") não é suportado!");
			
		}
		
		Iterator<Restaurante> itr = restaurantes.iterator();
		
		while (itr.hasNext()) {
			
			Restaurante restaurante = itr.next();
			double taxaEntrega = restaurante.getTaxaEntrega().doubleValue();
			
			if (filter.isEntregaGratis() && taxaEntrega > 0) {
				itr.remove();
			}
			
		}
		
		RestauranteComparator comparator = new RestauranteComparator(filter, SecurityUtils.loggedCliente().getCep());
		
		restaurantes.sort(comparator);
		
		return restaurantes;
		
	}

	@Transactional
	public void saveItemCardapio(ItemCardapio itemCardapio) {
		itemCardapio = itemCardapioRepository.save(itemCardapio);
		itemCardapio.setImagemFileName();
		imageService.uploadImagemItemCardapio(itemCardapio.getImagemFile(), itemCardapio.getImagem());
	}
	
}
