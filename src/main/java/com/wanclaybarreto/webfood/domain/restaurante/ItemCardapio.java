package com.wanclaybarreto.webfood.domain.restaurante;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.wanclaybarreto.webfood.infrastructure.web.validator.UploadConstraint;
import com.wanclaybarreto.webfood.util.FileType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "item_cardapio")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCardapio implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@NotBlank(message = "O nome não pode ser vazio.")
	private String nome;
	
	@NotBlank(message = "A descrição não pode ser vazia.")
	private String descricao;
	
	@NotBlank(message = "A categoria não pode ser vazia.")
	@Size(max = 30, message = "O nome da categoria não pode exceder 30 caracteres.")
	private String categoria;
	
	private String imagem;
	
	@UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG})
	private transient MultipartFile imagemFile;
	
	@NotNull(message = "O preço não pode ser vazio.")
	@Min(value = 0, message = "O preço não pode ser menor que 0,00 (zero).")
	private BigDecimal preco;
	
	@NotNull
	private Boolean destaque;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "restaurante_id")
	private Restaurante restaurante;
	
	public void setImagemFileName() {
		if (getId() == null) {
			throw new IllegalStateException("É preciso primeiro gravar o registro (Item do Cardápio).");
		}
		
		this.imagem = getId() + "-itemcardapio." + FileType.of(imagemFile.getContentType()).getExtension();
	}
	
}
