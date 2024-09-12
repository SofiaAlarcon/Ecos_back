package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Pais;

public interface IPaisRepositorio extends JpaRepository<Pais, Long>{

	Pais findByNombre(String string);

}
