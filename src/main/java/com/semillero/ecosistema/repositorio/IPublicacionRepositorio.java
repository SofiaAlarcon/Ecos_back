package com.semillero.ecosistema.repositorio;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.semillero.ecosistema.entidad.Publicacion;

public interface IPublicacionRepositorio extends JpaRepository<Publicacion, Long>{

	List<Publicacion> findAllByDeletedFalse();
	
	@Query("SELECT new map(p.id as id, p.titulo as titulo, p.fechaDeCreacion as fechaDeCreacion, p.cantidadDeVisualizaciones as visualizaciones) FROM Publicacion p")
    List<Map<String, Object>> findAllVisualizacionesAndDetails();
}
