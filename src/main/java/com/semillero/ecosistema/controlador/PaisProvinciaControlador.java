package com.semillero.ecosistema.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.semillero.ecosistema.dto.PaisDto;
import com.semillero.ecosistema.entidad.Provincia;
import com.semillero.ecosistema.servicio.PaisProvinciaServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "País y provincias", description = "Operaciones relacionadas con países y sus provincias")
@RestController
@RequestMapping("/ubicacion")
@Validated
public class PaisProvinciaControlador {

	@Autowired
	private PaisProvinciaServiceImpl paisProvinciaServiceImpl;
	
	@Operation(summary = "Países", description = "Devuelve una lista de todos los países")
	@GetMapping("/paises")
    public List<PaisDto> getAllPaises() {
        return paisProvinciaServiceImpl.mostrarTodo();
    }
	
	@Operation(summary = "Provincias", description = "Devuelve una lista de las provincias de un país determinado")
	@GetMapping("/paises/{paisId}/provincias")
	public ResponseEntity<?>mostrarProvinciasPorId(@PathVariable Long paisId){
		List<Provincia>provincia=paisProvinciaServiceImpl.mostrarProvinciasPorPaisId(paisId);
		if(provincia.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El id proporcionado no existe");
		}else {
			return ResponseEntity.ok(provincia);
		}
	}
}
