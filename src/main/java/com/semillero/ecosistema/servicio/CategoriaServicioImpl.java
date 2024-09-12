package com.semillero.ecosistema.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.repositorio.ICategoriaRepositorio;

@Service
public class CategoriaServicioImpl {
	@Autowired
	private ICategoriaRepositorio categoriaRepositorio;
	
	public List<Categoria> getCategorias(){
		return categoriaRepositorio.findAll();
	}
	
	public Categoria buscarPorId(Long id) {
		Optional<Categoria> opc=categoriaRepositorio.findById(id);
		return  opc.orElse(null);
	}
}
