package com.wanclaybarreto.webfood.domain.restaurante;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer> {

	public List<ItemCardapio> findAllByRestaurante_IdOrderByNome(Integer restauranteId);

	public List<ItemCardapio> findAllByRestaurante_IdAndDestaqueOrderByNome(Integer restauranteId, Boolean destaque);

	public List<ItemCardapio> findAllByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(
			Integer restauranteId, Boolean destaque, String categoria
	);

	@Query("select distinct ic.categoria from ItemCardapio ic where ic.restaurante.id = ?1 order by ic.categoria")
	public List<String> findCategorias(Integer restauranteId);

	@Query("select ip.itemCardapio.nome, sum(ip.quantidade), sum(ip.quantidade * ip.preco) " +
            "from Pedido p inner join p.itens ip " +
            "where p.restaurante.id = ?1 and ip.itemCardapio.id = ?2 and p.dataHora between ?3 and ?4 " +
            "group by ip.itemCardapio.id")
	public List<Object[]> findItensFaturamento(Integer restauranteId, Integer itemCardapioId, LocalDateTime dataInicial, LocalDateTime dataFinal);

	@Query("select ip.itemCardapio.nome, sum(ip.quantidade), sum(ip.quantidade * ip.preco) " +
            "from Pedido p inner join p.itens ip " +
            "where p.restaurante.id = ?1 and p.dataHora between ?2 and ?3 " +
            "group by ip.itemCardapio.id")
	public List<Object[]> findItensFaturamento(Integer restauranteId, LocalDateTime dataInicial, LocalDateTime dataFinal);

}
