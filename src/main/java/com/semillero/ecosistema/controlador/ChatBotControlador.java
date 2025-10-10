package com.semillero.ecosistema.controlador;

import java.util.Optional;

import org.checkerframework.checker.units.qual.degrees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.semillero.ecosistema.entidad.Pregunta;
import com.semillero.ecosistema.entidad.Respuesta;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Usuario.RolDeUsuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;
import com.semillero.ecosistema.servicio.ChatBotServicio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Chat bot", description = "Operaciones relacionadas con el chat bot")
@RestController
@RequestMapping
public class ChatBotControlador {
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	@Autowired
	private ChatBotServicio chatBotServicio;
	
	@Operation(summary = "Obtener pregunta", description = "Muestra la respuesta correspondiente a la pregunta seleccionada por el usuario")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Respuesta.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
	})
	@GetMapping(value="/pregunta", params="preguntaId")
	public ResponseEntity<Respuesta> obtenerRespuesta(@Parameter(description = "ID de la pregunta seleccionada por el usuario", example = "1") @RequestParam Long preguntaId) {
		Respuesta respuesta = chatBotServicio.mostrarRespuesta(preguntaId);
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Enviar pregunta", description = "Permite que usuarios autenticados con rol 'USUARIO' envíen preguntas")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType ="application/json", schema = @Schema(type = "string"))),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró ningún usuario con el ID indicado", content = @Content),
		@ApiResponse(responseCode = "403", description = "Unauthorized. El usuario no cuenta con los permisos requeridos para enviar una pregunta", content = @Content)
	})
	@PreAuthorize("hasRole('USUARIO')")
	@PostMapping(value="/pregunta/usuario/{usuarioId}")
	public ResponseEntity<String> enviarPregunta(@Parameter(description = "ID del usuario", example = "1")
	@PathVariable Long usuarioId,
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Contenido de la consulta",
        required = true,
        content = @Content(schema = @Schema(implementation = Pregunta.class))
    )
	@RequestBody Pregunta pregunta) {
		Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);
		if (usuario.isPresent() && usuario.get().getRol() == RolDeUsuario.USUARIO) {
			chatBotServicio.guardarPregunta(usuarioId, pregunta);
			return ResponseEntity.ok("Pregunta enviada con éxito");
		} else {
			return ResponseEntity.badRequest().body("No se encontró ningún usuario con el id proporcionado o con los permisos requeridos");
		}
	}
}