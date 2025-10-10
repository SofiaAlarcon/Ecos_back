package com.semillero.ecosistema.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.PublicacionDto;
import com.semillero.ecosistema.entidad.Imagen;
import com.semillero.ecosistema.entidad.Publicacion;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Usuario.RolDeUsuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;
import com.semillero.ecosistema.servicio.ImagenServicioImpl;
import com.semillero.ecosistema.servicio.PublicacionServicioImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Publicación", description = "Operaciones relacionadas con las publicaciones")
@RestController
public class PublicacionControlador {

	@Autowired
	private PublicacionServicioImpl publicacionServicioImpl;
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	@Autowired
	private ImagenServicioImpl imagenServicioImpl;

	@Operation(summary = "Publicar", description = "Permite a los usuarios registrados con rol 'ADMIN' crear publicaciones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Publicación creada", content = @Content),
			@ApiResponse(responseCode = "403", description = "El usuario no cuenta con los permisos necesarios. Debe tener el rol 'ADMIN'", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encontró ningún usuario con el ID indicado", content = @Content)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/publicar/{userId}", consumes = "multipart/form-data")
	public ResponseEntity<String> crearPublicacion(
			@Parameter(description = "ID del usuario que creará la publicación", example = "1") @PathVariable Long userId,
			@Valid @ModelAttribute PublicacionDto publicacionDto, @RequestPart("imagen") List<MultipartFile> files)
			throws IOException {

		Optional<Usuario> user = usuarioRepositorio.findById(userId);

		if (user.isPresent() && user.get().getRol() == RolDeUsuario.ADMIN) {
			// Se fija que el tamaño del array de files que llega no sea mayor que tres, si
			// es así devuelve un mensaje de error
			if (files.size() > 3) {
				return ResponseEntity.badRequest().body("No se pueden subir más de 3 imágenes");
			} else {
				// si el tamaño del array de files es menor o igual a 3, continúa normalmente
				// con la creación de la publicación
				List<ImageModel> imageModels = new ArrayList<>();

				// Crear ImageModel para cada archivo
				for (MultipartFile file : files) {
					String nombreArchivo = file.getOriginalFilename();

					ImageModel imageModel = new ImageModel();
					imageModel.setFile(file);
					imageModel.setNombre(nombreArchivo);

					imageModels.add(imageModel);
				}

				// Pasar la lista de ImageModel al servicio de publicaciones
				publicacionDto.setUsuarioCreador(user.get());
				publicacionServicioImpl.crearPublicacion(publicacionDto, imageModels);

				return ResponseEntity.ok("Publicación creada con éxito");
			}

		}

		return ResponseEntity.badRequest()
				.body("No se encontró ningún usuario con el id proporcionado o con los permisos requeridos");
	}

	@Operation(summary = "Publicar", description = "permite a los usuarios registrados con rol 'ADMIN' editar publicaciones existentes")
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/editar-publicacion/publicacion/{publicacionId}", consumes = "multipart/form-data")
	public ResponseEntity<?> editarPublicacion(@PathVariable Long publicacionId,
			@ModelAttribute PublicacionDto publicacionEditada) {
		try {
			Publicacion publicacionActualizada = publicacionServicioImpl.editarPublicacion(publicacionId,
					publicacionEditada);
			return ResponseEntity.status(HttpStatus.OK).body(publicacionActualizada);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@Operation(summary = "Publicar", description = "permite a los usuarios registrados con rol 'ADMIN' eliminar imágenes de publicaciones existentes")
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/eliminar/{imagenId}")
	public ResponseEntity<?> eliminarImagen(@PathVariable Long imagenId) {
		try {
			imagenServicioImpl.eliminarImagen(imagenId);
			return ResponseEntity.ok("Imagen eliminada correctamente");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@Operation(summary = "Actualizar imagen", description = "permite a los usuarios registrados con rol 'ADMIN' actualizar imágenes de publicaciones existentes")
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/actualizarImagen/{imagenId}")
	public ResponseEntity<?> actualizarImagen(@PathVariable Long imagenId, @RequestParam("file") MultipartFile file) {
		try {
			// Llamar al servicio para actualizar la imagen
			Imagen imagenActualizada = imagenServicioImpl.actualizarImagen(imagenId, file);
			return ResponseEntity.ok(imagenActualizada);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@Operation(summary = "Borrar publicación", description = "Permite a los usuarios registrados con rol 'ADMIN' eliminar publicaciones existentes a partir de un ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Publicación borrada"),
			@ApiResponse(responseCode = "404", description = "No se encontró la publicación indicada")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/borrar-publicacion/{id}")
	public ResponseEntity<String> borrarPublicacion(
			@Parameter(description = "ID de la publicación a borrar", example = "1") @PathVariable Long id) {
		boolean success = publicacionServicioImpl.borrarPublicacion(id);

		if (success) {
			return ResponseEntity.ok("La publicación se borró con exito");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No se encontró una publicación con el id proporcionado");
		}
	}

	@Operation(summary = "Obtener publicaciones", description = "Permite a los usuarios registrados con rol 'ADMIN' obtener una lista con todas las publicaciones, tanto las activas como las que han sido borradas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Devuelve una lista con las publicaciones", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Publicacion.class)))),
			@ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario debe tener rol 'ADMIN'", content = @Content)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/publicaciones")
	public ResponseEntity<List<Publicacion>> obtenerPublicaciones() {
		return ResponseEntity.ok(publicacionServicioImpl.obtenerPublicaciones());
	}

	@Operation(summary = "Obtener publicaciones activas", description = "Permite que los usuarios que no se hayan registrado puedan ver las publicaciones activas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Devuelve una lista de las publicaciones activas", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Publicacion.class)))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
	})
	@GetMapping(value = "/publicaciones/activas")
	public ResponseEntity<List<Publicacion>> obtenerPublicacionesActivas() {
		List<Publicacion> publicacionesActivas = publicacionServicioImpl.obtenerPublicacionesActivas();

		return ResponseEntity.ok(publicacionesActivas);
	}

	@Operation(summary = "Buscar publicación", description = "permite buscar una publicación a partir de un ID determinado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Devuelve la publicación correspondiente al ID brindado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Publicacion.class))),
			@ApiResponse(responseCode = "404", description = "No se encontró la publicación indicada", content = @Content)
	})
	@GetMapping(value = "buscar/{idPublicacion}")
	public ResponseEntity<?> buscarPorId(
			@Parameter(description = "ID de la publicación a buscar", example = "1") @PathVariable Long idPublicacion) {
		Optional<Publicacion> publicacion = publicacionServicioImpl.buscarPublicacionPorId(idPublicacion);

		if (publicacion.isPresent()) {
			return ResponseEntity.ok(publicacion.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("No se encontró ninguna publicación con el id proporcionado");

	}

	@Operation(summary = "Incrementa las visualizaciones de una publicación determinada", description = "Al buscar una publicación por ID, si esta existe, entonces automáticamente incrementa en 1 su cantidad de visualizaciones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Incrementa las visualizaciones de la publicación indicada", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encontró la publicación indicada", content = @Content)
	})
	@GetMapping(value = "incrementarVisualizaciones/{idPublicacion}")
	public ResponseEntity<?> incrementarVisualizaciones(
			@Parameter(description = "ID de la publicación", example = "1") @PathVariable Long idPublicacion) {
		Optional<Publicacion> publicacion = publicacionServicioImpl.buscarPublicacionPorId(idPublicacion);

		if (publicacion.isPresent()) {
			Publicacion pub = publicacion.get();
			publicacionServicioImpl.incrementarVisualizaciones(pub);
			return ResponseEntity.ok().body("Visualizaciones incrementadas");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("No se encontró ninguna publicación con el id proporcionado");
	}

	@Operation(summary = "Cambiar el estado de una publicación", description = "Permite que los usuarios registrados con rol 'ADMIN' cambien el estado de una publicación determinada a partir de su ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Se cambió el estado de la publicación indicada", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encontró la publicación indicada", content = @Content)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/cambiar-estado/{id}")
	public ResponseEntity<String> cambiarEstado(
			@Parameter(description = "ID de la publicación", example = "1") @PathVariable Long id) {
		boolean success = publicacionServicioImpl.cambiarEstado(id);

		if (success == true) {
			return ResponseEntity.ok("El estado se cambio con éxito");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No se encontró una publicación con el id proporcionado");
		}
	}

}
