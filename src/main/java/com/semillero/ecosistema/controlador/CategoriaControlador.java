package com.semillero.ecosistema.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.servicio.CategoriaServicioImpl;

@RestController
public class CategoriaControlador {
	@Autowired
	private CategoriaServicioImpl categoriaServicioImpl;
	
	
	@GetMapping(value="/categorias")
	public ResponseEntity<List<Categoria>> getListaCategorias() {
		return ResponseEntity.ok(categoriaServicioImpl.getCategorias());
	}
}
