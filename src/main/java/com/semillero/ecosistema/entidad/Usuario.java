package com.semillero.ecosistema.entidad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {

	public enum RolDeUsuario {
		ADMIN,
		USUARIO
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "El nombre no puede estar en blanco")
	private String nombre;
	
	@NotBlank(message = "El apellido no puede estar en blanco")
	private String apellido;
	
	@NotBlank(message = "El email no puede estar en blanco")
	@Email
	private String email;
	
	@NotNull
	@Column(columnDefinition = "boolean default false")
	private boolean deleted = false;
	
	@Enumerated(EnumType.STRING)
	private RolDeUsuario rol;
	
	
}
