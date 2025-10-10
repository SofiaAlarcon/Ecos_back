package com.semillero.ecosistema.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.repositorio.ICategoriaRepositorio;
import com.semillero.ecosistema.servicio.DashboardServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Dashboard", description = "Operaciones relacionadas con el dashboard de administrador")
@RestController
public class DashboardControlador {

	@Autowired
	private DashboardServicio dashboardServicio;

	@Autowired
	private ICategoriaRepositorio categoriaRepositorio;

	@Operation(summary = "Estadísticas de proveedores", description = "Devuelve una lista con las estadísticas de los proveedores")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"aceptados\":12,\"enRevision\":4,\"denegados\":6,\"total\":22}"))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
	})
	@GetMapping("/estadisticasProveedores")
	public Map<String, Long> obtenerEstadisticasProveedores() {
		Map<String, Long> estadisticas = new HashMap<>();
		estadisticas.put("aceptados", dashboardServicio.proveedoresAceptados());
		estadisticas.put("enRevision", dashboardServicio.proveedoresEnEspera());
		estadisticas.put("denegados", dashboardServicio.proveedoresDenegados());
		estadisticas.put("total", dashboardServicio.proveedorTotal());

		return estadisticas;
	}

	@Operation(summary = "Estadísticas por categoría", description = "Devuelve una lista con la cantidad de proveedores para cada categoría, ordenada alfabeticamente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(type = "oject", example = "{\"Gastronomía\":8,\"Cosmética\":2,\"Hogar y Jardín\":6,\"total\":16}"))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
	})
	@GetMapping("/proveedoresPorCategoria")
	public Map<String, Long> obtenerEstadisticasPorCategoria() {
		Map<String, Long> estadisticas = new HashMap<>();
		List<Categoria> categorias = categoriaRepositorio.findAll();

		for (Categoria categoria : categorias) {
			estadisticas.put(categoria.getNombre(), dashboardServicio.contarProveedoresPorCategoria(categoria));
		}

		// Ordenar el mapa por claves (nombres de categorías)
		Map<String, Long> sortedEstadisticas = new TreeMap<>(estadisticas);

		return sortedEstadisticas;
	}

	@Operation(summary = "Visualizaciones", description = "Devuelve una lista con los detalles de todas las publicaciones, entre ellos las visualizaciones")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "operación exitosa", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "object", example = "{\"id\": 1, \"titulo\": \"Publicación de prueba\", \"fechaDeCreacion\": \"2025-05-15\", \"visualizaciones\": 33}")))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
	})
	@GetMapping("/visualizaciones")
	public List<Map<String, Object>> obtenerDetallesDeTodas() {
		return dashboardServicio.obtenerDetallesDeTodasLasPublicaciones();
	}

}
