package com.semillero.ecosistema.dto;

import com.semillero.ecosistema.entidad.Proveedor.EstadoProveedor;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatusDto {

	
	private Long id;
	
	private EstadoProveedor estado;
	
	private String feedback;
}
