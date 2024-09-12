package com.semillero.ecosistema.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.Respuesta;
import com.semillero.ecosistema.repositorio.IRespuestaRepositorio;

@Component
@Order(6)
public class RespuestaDataLoader implements CommandLineRunner {

	@Autowired
	private IRespuestaRepositorio respuestaRepositorio;
	
	@Override
	public void run(String... args) throws Exception {
		loadRespuestaChatBotData();
	}
	
	private void loadRespuestaChatBotData() {
		if (respuestaRepositorio.count() == 0) {
			//crea las Respuestas con sus datos
			Respuesta respuesta1 = new Respuesta();
			respuesta1.setTitulo ("No, no tiene ningún costo ser proveedor.");
			
			Respuesta respuesta2 = new Respuesta();
			respuesta2.setTitulo("La categoría la decidís al momento de cargar tu producto o servicio.");
			
			Respuesta respuesta3 = new Respuesta();
			respuesta3.setTitulo("No, no apareces inmediatamente. Debes pasar por un proceso de revisión por parte del administrador para ser aceptado. En caso de que el administrador tenga alguna duda, colocará tu servicio en revisión para que puedas responder o aclarar las inquietudes del administrador.");
			
			Respuesta respuesta4 = new Respuesta();
			respuesta4.setTitulo("Si, siempre podés editar lo que subís. Tené en cuenta que vuelves a un proceso de revisión en caso de editar tu postulación.");
			
			Respuesta respuesta5 = new Respuesta();
			respuesta5.setTitulo("No, no puedes cargar publicaciones. Las publicaciones las carga el administrador.");
			
			Respuesta respuesta6 = new Respuesta();
			respuesta6.setTitulo("Actualmente no es posible abonar un servicio “Premium”. Los proveedores le aparecen al visitante por cercanía en caso de que hayan activado su ubicación. Así será más fácil que los usuarios cercanos a tu emprendimiento te encuentren.");
			
			Respuesta respuesta7 = new Respuesta();
			respuesta7.setTitulo("Si! Podes cargar mas de un producto o servicio. Siempre por separado.");
			
			//guarda las Respuestas
			respuestaRepositorio.save(respuesta1);
			respuestaRepositorio.save(respuesta2);
			respuestaRepositorio.save(respuesta3);
			respuestaRepositorio.save(respuesta4);
			respuestaRepositorio.save(respuesta5);
			respuestaRepositorio.save(respuesta6);
			respuestaRepositorio.save(respuesta7);
			
		}
	}
}
