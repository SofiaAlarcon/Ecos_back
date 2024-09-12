package com.semillero.ecosistema.entidad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "El título no puede estar en blanco")
	private String titulo;
	
	@NotBlank(message = "La descripción no puede estar en blanco")
	@Size(max=2500, message = "La descripción no debe superar los 2500 caracteres")
	private String descripcion;
	
	@NotNull
	@Column(columnDefinition = "boolean default false")
	private boolean deleted = false;
	
	
	private Date fechaDeCreacion;
	
	@OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Imagen> imagenes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario.id")
	private Usuario usuarioCreador;
	
	@NotNull
	private Integer cantidadDeVisualizaciones = 0;
}
