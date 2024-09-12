package com.semillero.ecosistema.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Pregunta;
import com.semillero.ecosistema.entidad.Respuesta;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.repositorio.IPreguntaRepositorio;
import com.semillero.ecosistema.repositorio.IRespuestaRepositorio;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;

@Service
public class ChatBotServicio {
	
	@Autowired
	private IPreguntaRepositorio preguntaRepositorio;
	@Autowired
	private IRespuestaRepositorio respuestaRepositorio;
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	public Respuesta mostrarRespuesta(Long preguntaId) {
		Optional<Respuesta>respuesta= respuestaRepositorio.findById(preguntaId);
		return respuesta.orElse(null);
	}
	
	public Pregunta guardarPregunta(Long usuarioId, Pregunta pregunta) {
		//Busca el usuario correspondiente al id ingresada
		Optional<Usuario> usuarioCreador = usuarioRepositorio.findById(usuarioId);
		//Setea el usuario encontrado como el usuario creador de la pregunta
		pregunta.setUsuarioCreador(usuarioCreador.get());
		return preguntaRepositorio.save(pregunta);	
	}

}
