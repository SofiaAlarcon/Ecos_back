package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.semillero.ecosistema.entidad.Categoria;

public interface ICategoriaRepositorio extends JpaRepository<Categoria,Long>{

	Categoria findByNombre(String nombre);

}
