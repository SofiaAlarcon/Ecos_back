package com.semillero.ecosistema.dto;

import com.semillero.ecosistema.dto.ProveedorDto.EstadoProveedorDTO;
import com.semillero.ecosistema.entidad.Proveedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RevisionDto {
	
	private Proveedor.EstadoProveedor estado;
	
	private String feedback;

}
