package com.semillero.ecosistema.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.repositorio.ICategoriaRepositorio;
import com.semillero.ecosistema.servicio.DashboardServicio;


@RestController
public class DashboardControlador {

	@Autowired
	private DashboardServicio dashboardServicio;
	
	@Autowired
	private ICategoriaRepositorio categoriaRepositorio;
	
	
	@GetMapping("/estadisticasProveedores")
    public Map<String, Long> obtenerEstadisticasProveedores() {
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("aceptados", dashboardServicio.proveedoresAceptados());
        estadisticas.put("enRevision", dashboardServicio.proveedoresEnEspera());
        estadisticas.put("denegados", dashboardServicio.proveedoresDenegados());
        estadisticas.put("total", dashboardServicio.proveedorTotal());

        return estadisticas;
    }
	
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
	
	@GetMapping("/visualizaciones")
	public List<Map<String, Object>> obtenerDetallesDeTodas() {
	    return dashboardServicio.obtenerDetallesDeTodasLasPublicaciones();
	}
	
}
