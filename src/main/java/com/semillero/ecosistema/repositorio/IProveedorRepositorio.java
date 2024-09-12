package com.semillero.ecosistema.repositorio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Usuario;

public interface IProveedorRepositorio extends JpaRepository<Proveedor, Long>{

	int countByUsuarioId(Long usuarioId);
	List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
	
	List<Proveedor> findByCategoriaId(Long categoriaId);
	List<Proveedor> findByUsuario(Usuario usuarioCreador);
	
	List<Proveedor> findByEstadoAndDeletedFalseAndFechaCreacionAfter(
	            Proveedor.EstadoProveedor estado,
	            LocalDateTime fechaCreacion);
	long countByEstado(Proveedor.EstadoProveedor estado);
	
	long countByEstadoIn(List<Proveedor.EstadoProveedor> estados);
	
	long countByCategoria(Categoria categoria);
}
