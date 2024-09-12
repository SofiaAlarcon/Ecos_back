package com.semillero.ecosistema.servicio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.entidad.Proveedor.EstadoProveedor;
import com.semillero.ecosistema.repositorio.IProveedorRepositorio;
import com.semillero.ecosistema.repositorio.IPublicacionRepositorio;

@Service
public class DashboardServicio {
	
	@Autowired
	private IProveedorRepositorio proveedorRepositorio;
	
	@Autowired
	private IPublicacionRepositorio publicacionRepositorio;
	
	public Long proveedoresAceptados() {
		return proveedorRepositorio.countByEstado(EstadoProveedor.ACEPTADO);
	}
	
	 public Long proveedoresEnEspera() {
	        List<EstadoProveedor> estados = Arrays.asList(
	            EstadoProveedor.REVISION_INICIAL, 
	            EstadoProveedor.REQUIERE_CAMBIOS, 
	            EstadoProveedor.CAMBIOS_REALIZADOS
	        );
	        return proveedorRepositorio.countByEstadoIn(estados);
	    }
	
	public Long proveedoresDenegados() {
		return proveedorRepositorio.countByEstado(EstadoProveedor.DENEGADO);
	}
	
	public Long proveedorTotal() {
		return proveedorRepositorio.count();
	}

	public Long contarProveedoresPorCategoria(Categoria categoria) {
		return proveedorRepositorio.countByCategoria(categoria);
	}
	
	public List<Map<String, Object>> obtenerDetallesDeTodasLasPublicaciones() {
        return publicacionRepositorio.findAllVisualizacionesAndDetails();
    }
	
}
