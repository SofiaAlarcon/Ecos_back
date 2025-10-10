package com.semillero.ecosistema.controlador;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.C;
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
import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.ProveedorDto;
import com.semillero.ecosistema.dto.RevisionDto;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Imagen;
import com.semillero.ecosistema.servicio.ImagenServicioImpl;
import com.semillero.ecosistema.servicio.ProveedorServicio;
import com.semillero.ecosistema.servicio.UsuarioServicioImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Proveedores", description = "Operaciones relacionadas con los proveedores")
@RestController
@RequestMapping
public class ProveedorControlador {

	@Autowired
	private ProveedorServicio proveedorServicio;
	
	@Autowired
	private UsuarioServicioImpl usuarioServicio;
	
	
	@Autowired
	private ImagenServicioImpl imagenServicioImpl;

	@Operation(summary = "Crear proveedor", description = "Permite que usuarios registrados creen proveedores")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(example = "\"Proveedor creado con éxito\""))),
		@ApiResponse(responseCode = "404", description= "Not found. No se encontró ningún usuario con el id indicado", content = @Content(mediaType = "application/json", schema = @Schema(example= "\"Usuario no encontrado\"")) )
	})
	@PreAuthorize("hasRole('USUARIO')")
	@PostMapping(value="/crearProveedor/usuario/{usuarioId}",consumes = "multipart/form-data")
	public ResponseEntity<?> crearProveedor(@PathVariable Long usuarioId,@ModelAttribute ProveedorDto proveedorDto,
	@Parameter(description = "Archivos de imagen del proveedor",
	required = true,
	content = @Content(
		mediaType = "application/octet-stream",
		schema = @Schema(type="string", format = "binary")
	))
	@RequestPart("imagenes") List<MultipartFile> files) {
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
	
	@Operation(summary = "Editar proveedor", description = "Permite que usuarios registrados editen sus proveedores")
	@Parameters({
		@Parameter(
			name = "usuarioId",
			description = "ID del usuario que editará el proveedor",
			required = true,
			example = "1"
		),
		@Parameter(
			name = "proveedorId",
			description = "ID del proveedor a editar",
			required = true,
			example = "1"
		)
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		description = "Datos del proveedor a editar", 
		required = true,
		content = @Content(
			mediaType = "multipart/form-data",
			schema = @Schema(implementation = ProveedorDto.class)
		)
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proveedor.class))),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró ningún usuario o ningún proveedor con los ID indicados", content = @Content)
	})
	@PreAuthorize("hasRole('USUARIO')")
	@PutMapping(value = "/editarProveedor/usuario/{usuarioId}/proveedor/{proveedorId}", consumes = "multipart/form-data")
	public ResponseEntity<?> editarProveedor(@PathVariable Long usuarioId,@PathVariable Long proveedorId, @ModelAttribute ProveedorDto proveedorDto) {

	    try {
	        // Llamar al servicio para editar el proveedor
	        Proveedor proveedorActualizado = proveedorServicio.editarProveedor(usuarioId,proveedorId, proveedorDto);
	        return ResponseEntity.ok(proveedorActualizado);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}
	
	@Operation(summary = "Eliminar imagen", description = "Permite que usuarios registrados eliminen una imagen existente")
	@Parameters(
		@Parameter(name = "imagenId", description = "ID de la imagen a editar", example = "1")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(example = "\"Proveedor creado con éxito\""))),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró ninguna imagen con el ID indicado", content = @Content)
	})
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
	
	@Operation(summary = "Editar imagen", description = "Permite que usuarios registrados editen una imagen existente")
	@Parameters({
		@Parameter(
			name = "imagenId", description = "ID de la imagen a editar", required = true, example = "1"),
		@Parameter(
			name = "file", description = "Imagen a subir", required = true, content = @Content(
				mediaType = "application/octet-stream", 
				schema = @Schema(type = "string", 
				format = "binary")
			)
		)
		
	})
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró ninguna imagen con el ID indicado", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad request. Error al subir el archivo", content = @Content)
	})
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

	@Operation(summary = "Buscar proveedores", description = "Devuelve una lista de proveedores a partir de un nombre")
	@Parameter(name = "query", description = "Nombre del proveedor a buscar", required = true, example = "Juan Lopez")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "application/json", 
			array = @ArraySchema(
				schema = @Schema(implementation = Proveedor.class)
			))),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontron proveedores con el nombre indicado", content = @Content)
	})
	@GetMapping("/buscar")
	public ResponseEntity<List<Proveedor>>buscarProveedoresPorNombre(@RequestParam String query){
		try {
			List<Proveedor> proveedores=proveedorServicio.buscarPorNombre(query);
			return ResponseEntity.ok(proveedores);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@Operation(summary = "Mostrar todo", description = "Devuelve una lista de todos los proveedores")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = Proveedor.class))
		)),
		@ApiResponse(responseCode = "204", description = "Not found. No se encontraron proveedores registrados", content = @Content)
	})
	@GetMapping("/mostrarTodo")
	public ResponseEntity<List<Proveedor>>mostrarTodo(){
		return ResponseEntity.ok(proveedorServicio.mostrarTodo());
	}

	@Operation(summary = "Buscar por ID", description = "Devuelve una lista de proveedores a partir de un ID")
	@Parameter(name = "proveedorId", description = "ID del proveedor a buscar", required = true, example = "1")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "application/json", 
			schema = @Schema(implementation = Proveedor.class)
			)
		),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró ningún proveedor con el ID indicado", content = @Content)
	})
	@GetMapping("/buscarPorId/{proveedorId}")
	public ResponseEntity<Proveedor> buscarProveedorPorId(@PathVariable Long proveedorId) throws Exception {
		return ResponseEntity.ok(proveedorServicio.buscarProveedorPorId(proveedorId));
	}
	
	@Operation(summary = "Buscar por categoría", description = "Devuelve una lista de proveedores a partir de una categoría")
	@Parameter(name = "categoriaId", description = "ID de la categoría de la cual se quieren buscar los proveedores", required = true, example = "Gastronomía")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = Proveedor.class))
		)),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontraron proveedores para la categoría indicada", content = @Content)
	})
	@GetMapping("/buscarPorCategoria/{categoriaId}")
    public ResponseEntity<List<Proveedor>> buscarProveedoresPorCategoria(@PathVariable Long categoriaId) {
		return ResponseEntity.ok(proveedorServicio.buscarPorCategoriaId(categoriaId));
	}
	
	@Operation(summary = "Mostrar activos", description = "Devuelve una lista de proveedores activos")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = Proveedor.class))
		)),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontraron proveedores activos", content = @Content)
	})
	@GetMapping("/mostrarProveedorActivo")
	public ResponseEntity<List<Proveedor>> mostrarProveedorActivo(){
		return ResponseEntity.ok(proveedorServicio.mostrarProveedoresActivos());
	}
	

	@Operation(summary = "Proveedor nuevo", description = "Muestra al usuario ADMIN una lista de los proveedores postulados")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = Proveedor.class))
		)),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontrarion proveedores nuevos", content = @Content)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/nuevoProveedor")
	public ResponseEntity<List<Proveedor>>mostrarProveedorNuevo(){
		return ResponseEntity.ok(proveedorServicio.mostrarProveedorNuevo());
	}
	
	@Operation(summary = "Editar estado", description = "Permite que el ADMIN edite el estado de un proveedor determinado")
	@Parameter(name = "id", description = "ID del proveedor cuyo estado se desea modificar", required = true, example = "1")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del proveedor a editar", required = true, content = @Content(
		mediaType = "multipart/form-data",
		schema = @Schema(implementation = ProveedorDto.class)
	))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(
			mediaType = "application/json",
			schema = @Schema(
				type = "sting",
				description = "Nuevo estado del proveedor",
				allowableValues = {" REVISION_INICIAL","ACEPTADO","DENEGADO", "REQUIERE_CAMBIOS", "CAMBIOS_REALIZADOS"},
				example = "ACTIVO"
			)
		)),
		@ApiResponse(responseCode = "404", description = "Not found. No se encontró ningun proveedor con el ID indicado", content = @Content)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/editarEstado/{id}")
	public ResponseEntity<?>editarEstado(@PathVariable Long id,@RequestBody RevisionDto revisionDto){
		Proveedor nuevoEstado=proveedorServicio.administrarProveedor(id, revisionDto.getEstado(), revisionDto.getFeedback());
		return ResponseEntity.ok(nuevoEstado);
	}
	
	@Operation(summary = "Mis estados", description = "Permite que el usuario registrado vea el estado de sus proveedores")
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
	
	@Operation(summary = "Proveedores cercanos", description = "Permite que cualquier usuario pueda ver una lista de los proveedores cercanos a su ubicación actual")
	@GetMapping(value = "/proveedoresCercanos")
	public List<Proveedor> obtenerProveedoresCercanos(@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lng) throws Exception{
		return proveedorServicio.obtenerProveedoresCercanos(lat, lng);
	}

	
	
	
}
