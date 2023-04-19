package com.wanclaybarreto.webfood.domain.pedido;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	
	public List<Pedido> findAllByCliente_IdOrderByDataHoraDesc(Integer clienteId);

	public List<Pedido> findAllByRestaurante_IdOrderByDataHoraDesc(Integer restauranteId);

	//Os ids de restaurante nos métodos abaixo evitam que restaurante listem pedidos que não são seus.

	public Pedido findByIdAndRestaurante_Id(Integer pedidoId, Integer restauranteId);

	@Query("select p from Pedido p where p.id = ?1 and p.restaurante.id = ?2 and p.dataHora >= ?3 order by p.dataHora desc")
	public Pedido findByIdAndRestauranteAndDataInicial(
			Integer pedidoId, Integer restauranteId, LocalDateTime dataInicial
	);

	@Query("select p from Pedido p where p.id = ?1 and p.restaurante.id = ?2 and p.dataHora <= ?3 order by p.dataHora desc")
	public Pedido findByIdAndRestauranteAndDataFinal(
			Integer pedidoId, Integer restauranteId, LocalDateTime dataFinal
	);

	@Query("select p from Pedido p where p.id = ?1 and p.restaurante.id = ?2 and p.dataHora between ?3 and ?4 order by p.dataHora desc")
	public Pedido findByIdAndRestauranteAndDataInicialAndDataFinal(
			Integer pedidoId, Integer restauranteId, LocalDateTime dataInicial, LocalDateTime dataFinal
	);

	@Query("select p from Pedido p where p.restaurante.id = ?1 and p.dataHora >= ?2 order by p.dataHora desc")
	public List<Pedido> findAllByRestauranteAndDataInicialOrderByDataHoraDesc(
			Integer restauranteId, LocalDateTime dataInicial
	);

	@Query("select p from Pedido p where p.restaurante.id = ?1 and p.dataHora <= ?2 order by p.dataHora desc")
	public List<Pedido> findAllByRestauranteAndDataFinalOrderByDataHoraDesc(
			Integer restauranteId, LocalDateTime dataFinal
	);

	@Query("select p from Pedido p where p.restaurante.id = ?1 and p.dataHora between ?2 and ?3 order by p.dataHora desc")
	public List<Pedido> findAllByRestauranteAndDataInicialAndDataFinalOrderByDataHoraDesc(
			Integer restauranteId, LocalDateTime dataInicial, LocalDateTime dataFinal
	);
	
}
