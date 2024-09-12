package com.semillero.ecosistema.controlador;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencagedata.jopencage.model.JOpenCageResponse;
import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.CoordenadaDto;
import com.semillero.ecosistema.dto.ProveedorDto;
import com.semillero.ecosistema.dto.RevisionDto;

import com.semillero.ecosistema.dto.UbicacionDto;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.servicio.GeocodingService;

import com.semillero.ecosistema.entidad.Imagen;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.servicio.ImagenServicioImpl;

import com.semillero.ecosistema.servicio.ProveedorServicio;
import com.semillero.ecosistema.servicio.UsuarioServicioImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping
public class ProveedorControlador {

	@Autowired
	private ProveedorServicio proveedorServicio;
	
	@Autowired
	private UsuarioServicioImpl usuarioServicio;
	
	@Autowired
	private GeocodingService geocodingService;
	
	@Autowired
	private ImagenServicioImpl imagenServicioImpl;

	
	@PreAuthorize("hasRole('USUARIO')")
	@PostMapping(value="/crearProveedor/usuario/{usuarioId}",consumes = "multipart/form-data")
	public ResponseEntity<?> crearProveedor(@PathVariable Long usuarioId,@ModelAttribute ProveedorDto proveedorDto,@RequestPart("imagenes") List<MultipartFile> files) {
		try {
			List<ImageModel>imageModels=new ArrayList<>();
			for(MultipartFile file : files) {
				String nombreArchivo=file.getOriginalFilename();
				
				ImageModel imageModel= new ImageModel();
				imageModel.setFile(file);
				imageModel.setNombre(nombreArchivo);
				
				imageModels.add(imageModel);
				
			}
			
			proveedorDto.setUsuarioId(usuarioId);
			proveedorServicio.crearProveedor(usuarioId, proveedorDto,imageModels);
           
			return ResponseEntity.ok("Proveedor creado con éxito");
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('USUARIO')")
	@PutMapping(value = "/editarProveedor/usuario/{usuarioId}/proveedor/{proveedorId}", consumes = "multipart/form-data")
	public ResponseEntity<?> editarProveedor(
	        @PathVariable Long usuarioId,
	        @PathVariable Long proveedorId,
	        @ModelAttribute ProveedorDto proveedorDto) {

	    try {
			
	        // Llamar al servicio para editar el proveedor
	        Proveedor proveedorActualizado = proveedorServicio.editarProveedor(usuarioId,proveedorId, proveedorDto);
	        return ResponseEntity.ok(proveedorActualizado);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}
	
	@PreAuthorize("hasRole('USUARIO')")
	@DeleteMapping(value = "/eliminarImagen/{imagenId}")
	public ResponseEntity<?> eliminarImagen(@PathVariable Long imagenId) {
	    try {
	        imagenServicioImpl.eliminarImagen(imagenId);
	        return ResponseEntity.ok("Imagen eliminada correctamente");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}
	
	@PreAuthorize("hasRole('USUARIO')")
    @PutMapping("/actualizar/{imagenId}")
    public ResponseEntity<?> actualizarImagen(
            @PathVariable Long imagenId,
            @RequestParam("file") MultipartFile file) {
        try {
            // Llamar al servicio para actualizar la imagen
            Imagen imagenActualizada = imagenServicioImpl.actualizarImagen(imagenId, file);
            return ResponseEntity.ok(imagenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

	@GetMapping("/buscar")
	public ResponseEntity<List<Proveedor>>buscarProveedoresPorNombre(@RequestParam String query){
		try {
			List<Proveedor> proveedores=proveedorServicio.buscarPorNombre(query);
			return ResponseEntity.ok(proveedores);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/mostrarTodo")
	public ResponseEntity<List<Proveedor>>mostrarTodo(){
		return ResponseEntity.ok(proveedorServicio.mostrarTodo());
	}

	@GetMapping("/buscarPorId/{proveedorId}")
	public ResponseEntity<Proveedor> buscarProveedorPorId(@PathVariable Long proveedorId) throws Exception {
		return ResponseEntity.ok(proveedorServicio.buscarProveedorPorId(proveedorId));
	}
	
	@GetMapping("/buscarPorCategoria/{categoriaId}")
    public ResponseEntity<List<Proveedor>> buscarProveedoresPorCategoria(@PathVariable Long categoriaId) {
		return ResponseEntity.ok(proveedorServicio.buscarPorCategoriaId(categoriaId));
	}
	
	@GetMapping("/mostrarProveedorActivo")
	public ResponseEntity<List<Proveedor>> mostrarProveedorActivo(){
		return ResponseEntity.ok(proveedorServicio.mostrarProveedoresActivos());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/nuevoProveedor")
	public ResponseEntity<List<Proveedor>>mostrarProveedorNuevo(){
		return ResponseEntity.ok(proveedorServicio.mostrarProveedorNuevo());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/editarEstado/{id}")
	public ResponseEntity<?>editarEstado(@PathVariable Long id,@RequestBody RevisionDto revisionDto){
		Proveedor nuevoEstado=proveedorServicio.administrarProveedor(id, revisionDto.getEstado(), revisionDto.getFeedback());
		return ResponseEntity.ok(nuevoEstado);
	}
	
	@PreAuthorize("hasRole('USUARIO')")
	@GetMapping("/misEstados/{usuarioId}")
	public ResponseEntity<?> misEstados(@PathVariable Long usuarioId) {
		Usuario usuarioCreador = usuarioServicio.buscarPorId(usuarioId);
		if (usuarioCreador != null) {
			return ResponseEntity.ok(proveedorServicio.misEstados(usuarioCreador));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún usuario con el id proporcionado");
		}
		
	}
	
	@GetMapping(value = "/proveedoresCercanos")
	public List<Proveedor> obtenerProveedoresCercanos(@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lng) throws Exception{
		return proveedorServicio.obtenerProveedoresCercanos(lat, lng);
	}

	
	
	
}
