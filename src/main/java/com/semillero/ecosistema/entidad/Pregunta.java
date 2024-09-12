package com.semillero.ecosistema.entidad;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta {

	public enum EstadoPregunta {
		REVISION_INICIAL,
        RESUELTA
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "El texto no puede estar en blanco")
	@Column(columnDefinition = "VARCHAR(500)")
	private String titulo;
	
	@Enumerated(EnumType.STRING)
	private EstadoPregunta estado;
	
	@ManyToOne
	@JoinColumn(name= "id.usuario")
	private Usuario usuarioCreador;
	
	
	
}
