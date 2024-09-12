package com.semillero.ecosistema.dto;

import java.util.Date;
import java.util.List;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.entidad.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PublicacionDto {
	private Long id;
	private String titulo;
	private String descripcion;
	private boolean deleted = false;
	private Date fechaDeCreacion;
	private Usuario usuarioCreador;

}
