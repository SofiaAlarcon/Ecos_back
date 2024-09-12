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
public class UbicacionDto {
	private Long id;
	private String ciudad;
	private Provincia provincia;
	private Pais pais;
}
