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
import org.springframework.web.bind.annotation.RequestBody;
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

import jakarta.validation.Valid;

@RestController
public class PublicacionControlador {

	@Autowired
	private PublicacionServicioImpl publicacionServicioImpl;
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	@Autowired
	private ImagenServicioImpl imagenServicioImpl;
	


	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/publicar/{userId}", consumes = "multipart/form-data")
	public ResponseEntity<String> crearPublicacion(@PathVariable Long userId,@Valid @ModelAttribute PublicacionDto publicacionDto,@RequestPart("imagen") List<MultipartFile> files) throws IOException {

	    Optional<Usuario> user = usuarioRepositorio.findById(userId);
	    
	    if (user.isPresent() && user.get().getRol() == RolDeUsuario.ADMIN) {
	    	//se fija que el tamaño del array de files que llega no sea mayor que tres, si es así devuelve un mensaje de error
	    	if(files.size() > 3) {
	    		return ResponseEntity.badRequest().body("No se pueden subir más de 3 imágenes");
	    	} else {
	    		//si el tamaño del array de files es menor o igual a 3, continúa normalmente con la creación de la publicación
	    		List<ImageModel> imageModels = new ArrayList<>();
		        
		        //Crear ImageModel para cada archivo
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
	    
	    return ResponseEntity.badRequest().body("No se encontró ningún usuario con el id proporcionado o con los permisos requeridos");
	}


	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value="/editar-publicacion/publicacion/{publicacionId}", consumes = "multipart/form-data")
	public ResponseEntity<?> editarPublicacion(@PathVariable Long publicacionId, @ModelAttribute PublicacionDto publicacionEditada) {
		try {
			Publicacion publicacionActualizada = publicacionServicioImpl.editarPublicacion(publicacionId, publicacionEditada);
			return ResponseEntity.status(HttpStatus.OK).body(publicacionActualizada);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value ="/eliminar/{imagenId}")
	public ResponseEntity<?> eliminarImagen(@PathVariable Long imagenId) {
		try {
	        imagenServicioImpl.eliminarImagen(imagenId);
	        return ResponseEntity.ok("Imagen eliminada correctamente");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}
	
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
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value="/borrar-publicacion/{id}")
	public ResponseEntity<String> borrarPublicacion(@PathVariable Long id) {
		boolean success = publicacionServicioImpl.borrarPublicacion(id);
		
		if (success) {
			return ResponseEntity.ok("La publicación se borró con exito");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value="/publicaciones")
	public ResponseEntity<List<Publicacion>> obtenerPublicaciones() {
		return ResponseEntity.ok(publicacionServicioImpl.obtenerPublicaciones());
	}
	
	@GetMapping(value="/publicaciones/activas")
	public ResponseEntity<List<Publicacion>> obtenerPublicacionesActivas() {
		List<Publicacion> publicacionesActivas = publicacionServicioImpl.obtenerPublicacionesActivas();
		
		return ResponseEntity.ok(publicacionesActivas);
	}
	
	
	@GetMapping(value="buscar/{idPublicacion}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long idPublicacion) {
		Optional<Publicacion> publicacion = publicacionServicioImpl.buscarPublicacionPorId(idPublicacion);
		
		   if(publicacion.isPresent()){
		        return ResponseEntity.ok(publicacion.get());
		    } 
		   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna publicación con el id proporcionado");

	}
	
	@GetMapping(value = "incrementarVisualizaciones/{idPublicacion}")
	public ResponseEntity<?> incrementarVisualizaciones(@PathVariable Long idPublicacion) {
	    Optional<Publicacion> publicacion = publicacionServicioImpl.buscarPublicacionPorId(idPublicacion);
	    
	    if (publicacion.isPresent()) {
	        Publicacion pub = publicacion.get();
	        publicacionServicioImpl.incrementarVisualizaciones(pub);
	        return ResponseEntity.ok().body("Visualizaciones incrementadas");
	    } 
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna publicación con el id proporcionado");
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/cambiar-estado/{id}")
	public ResponseEntity<String> cambiarEstado(@PathVariable Long id) {
		boolean success = publicacionServicioImpl.cambiarEstado(id);

        if (success==true) {
            return ResponseEntity.ok("El estado se cambio con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
        }
	}
	

	
}
