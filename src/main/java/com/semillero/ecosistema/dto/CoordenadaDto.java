package com.semillero.ecosistema.dto;

import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.entidad.Provincia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoordenadaDto {
	private Long id;
	
	private double latitud;
	
	private double longitud;
}
