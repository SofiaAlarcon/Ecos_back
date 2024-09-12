package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Imagen;

public interface IImagenRepositorio extends JpaRepository<Imagen, Long> {

}
