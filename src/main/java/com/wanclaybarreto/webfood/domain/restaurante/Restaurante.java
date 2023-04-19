package com.wanclaybarreto.webfood.domain.restaurante;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.wanclaybarreto.webfood.domain.usuario.Usuario;
import com.wanclaybarreto.webfood.infrastructure.web.validator.UploadConstraint;
import com.wanclaybarreto.webfood.util.FileType;
import com.wanclaybarreto.webfood.util.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "restaurante")
public class Restaurante extends Usuario {
	
	@NotBlank(message = "O CNPJ não pode ser vazio.")
	@Pattern(regexp = "[0-9]{14}", message = "O CNPJ possui formato inválido.")
	@Column(length = 14, nullable = false)
	private String cnpj;
	
	private String logotipo;
	
	@UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG})
	private transient MultipartFile logotipoFile;
	
	@NotNull(message = "A taxa de entrega precisa ser especificada.")
	@Min(0)
	@Max(99)
	private BigDecimal taxaEntrega; //só vai poder receber valores contidos no intervalo [0, 99] por causa das anotações de validação min e max
	
	@NotNull(message = "O tempo de entrega precisa ser especificada.")
	@Min(0)
	@Max(120)
	private Integer tempoEntregaBase;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "restaurante_has_categoria",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "categoria_restaurante_id")
	)
	@Size(min = 1, message = "O restaurante precisa ter pelo menos uma categoria.")
	private Set<CategoriaRestaurante> categorias = new HashSet<>(0);
	
	@OneToMany(mappedBy = "restaurante")
	private Set<ItemCardapio> itensCardapio = new HashSet<>(0);
	
	public void setLogotipoFileName() {
		if (getId() == null) {
			throw new IllegalStateException("É preciso primeiro gravar o registro (restaurante).");
		}
		
		this.logotipo = getId() + "-logo." + FileType.of(logotipoFile.getContentType()).getExtension();
	}
	
	public String getCategoriasAsText() {
		Set<String> nomesCategorias = new LinkedHashSet<>();
		categorias.stream().forEach(c -> nomesCategorias.add(c.getNome()));
		
		return StringUtils.concatenate(nomesCategorias);
	}
	
	//TODO Implementar método para cálculo de tempo de entrega
	
}
