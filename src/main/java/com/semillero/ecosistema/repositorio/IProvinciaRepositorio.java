package com.semillero.ecosistema.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Provincia;

public interface IProvinciaRepositorio extends JpaRepository<Provincia, Long>{
	Optional<Provincia> findByIdAndPaisId(Long provinciaId, Long paisId);
	
	Provincia findByNombre(String string);
}
