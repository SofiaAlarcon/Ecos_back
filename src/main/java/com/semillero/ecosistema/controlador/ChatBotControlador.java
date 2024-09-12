package com.semillero.ecosistema.controlador;

import java.util.Optional;

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

@RestController
@RequestMapping
public class ChatBotControlador {
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	@Autowired
	private ChatBotServicio chatBotServicio;
	
	@GetMapping(value="/pregunta", params="preguntaId")
	public ResponseEntity<Respuesta> obtenerRespuesta(@RequestParam Long preguntaId) {
		Respuesta respuesta = chatBotServicio.mostrarRespuesta(preguntaId);
		return ResponseEntity.ok(respuesta);
	}
	
	@PreAuthorize("hasRole('USUARIO')")
	@PostMapping(value="/preguntar/usuario/{usuarioId}")
	public ResponseEntity<String> enviarPregunta(@PathVariable Long usuarioId, @RequestBody Pregunta pregunta) {
		Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);
		if (usuario.isPresent() && usuario.get().getRol() == RolDeUsuario.USUARIO) {
			chatBotServicio.guardarPregunta(usuarioId, pregunta);
			return ResponseEntity.ok("Pregunta enviada con éxito");
		} else {
			return ResponseEntity.badRequest().body("No se encontró ningún usuario con el id proporcionado o con los permisos requeridos");
		}
	}
}