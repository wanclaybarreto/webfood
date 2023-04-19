package com.wanclaybarreto.webfood.domain.restaurante;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {
	
	public Restaurante findByEmail(String email);
	
	public Restaurante findByCnpj(String cnpj);
	
	public List<Restaurante> findAllByNomeIgnoreCaseContaining(String nome);
	
	public List<Restaurante> findAllByCategorias_Id(Integer categoriaId);
	
}
