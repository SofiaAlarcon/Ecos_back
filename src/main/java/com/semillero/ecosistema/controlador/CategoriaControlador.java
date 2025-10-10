package com.semillero.ecosistema.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.servicio.CategoriaServicioImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Categoría", description="Operaciones relacionadas con las categorías")
@RestController
public class CategoriaControlador {
	@Autowired
	private CategoriaServicioImpl categoriaServicioImpl;
	
	@Operation(summary = "Obtener categorías", description = "Devuelve una lista de las categorías disponibles")
	@GetMapping(value="/categorias")
	public ResponseEntity<List<Categoria>> getListaCategorias() {
		return ResponseEntity.ok(categoriaServicioImpl.getCategorias());
	}
}
