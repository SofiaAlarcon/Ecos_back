package com.semillero.ecosistema.controlador;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.semillero.ecosistema.servicio.UsuarioServicioImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Usuario", description = "Operaciones relacionadas con los usuarios")
@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioControlador {

	@Autowired
	private UsuarioServicioImpl usuarioServicioImpl;
	
	@Operation(summary = "Desactivar usuario", description = "Permite desactivar un usuario determinado a partir de su ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró usuario con el ID proporcionado", content = @Content)
	})
	@PutMapping("/desactivar/{id}")
	public ResponseEntity<String> desactivarUsuario(@Parameter(description = "ID del usuario a desactivar", example = "1")@PathVariable Long id){
		boolean desactivado = usuarioServicioImpl.desactivarUsuario(id);

        if (desactivado) {
            return ResponseEntity.ok("El usuario se desactivó con éxito");
        } else {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya está desactivado o no se encontró el usuario con el id proporcionado");
        }
	}
}

