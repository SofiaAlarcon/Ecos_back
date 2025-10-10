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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaisDto.class)))
	}

	)
    public List<PaisDto> getAllPaises() {
        return paisProvinciaServiceImpl.mostrarTodo();
    }
	
	@Operation(summary = "Provincias", description = "Devuelve una lista de todas las provincias de un país determinado a partir de su ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Provincia.class)))),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró el país indicado", content = @Content)
	})
	@GetMapping("/paises/{paisId}/provincias")
	public ResponseEntity<?>mostrarProvinciasPorId(@Parameter(description = "ID del país del cual se obtendrá la lista de provincias", example = "1" )@PathVariable Long paisId){
		List<Provincia>provincia=paisProvinciaServiceImpl.mostrarProvinciasPorPaisId(paisId);
		if(provincia.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El ID proporcionado no existe");
		}else {
			return ResponseEntity.ok(provincia);
		}
	}
}
