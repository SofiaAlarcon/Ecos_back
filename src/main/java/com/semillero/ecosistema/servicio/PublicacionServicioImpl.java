package com.semillero.ecosistema.servicio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.ImageUploadResult;
import com.semillero.ecosistema.dto.PublicacionDto;
import com.semillero.ecosistema.entidad.Imagen;
import com.semillero.ecosistema.entidad.Publicacion;
import com.semillero.ecosistema.repositorio.IPublicacionRepositorio;


@Service
@Transactional
public class PublicacionServicioImpl {

	@Autowired
    private IPublicacionRepositorio publicacionRepositorio;
	
	@Autowired
	private ImagenServicioImpl imagenServicio;

	public Publicacion crearPublicacion(PublicacionDto publicacionDto, List<ImageModel> imageModels) throws IOException {
	    publicacionDto.setFechaDeCreacion(new Date());

	    Publicacion publicacionNueva = new Publicacion();
	    publicacionNueva.setTitulo(publicacionDto.getTitulo());
	    publicacionNueva.setDescripcion(publicacionDto.getDescripcion());
	    publicacionNueva.setDeleted(publicacionDto.isDeleted());
	    publicacionNueva.setFechaDeCreacion(publicacionDto.getFechaDeCreacion());
	    publicacionNueva.setUsuarioCreador(publicacionDto.getUsuarioCreador());

	    
	    List<Imagen> listaDeImagenes = new ArrayList<>();

	    // Procesar cada imagen
	    for (ImageModel imageModel : imageModels) {
	        if (imageModel.getFile() != null && !imageModel.getFile().isEmpty()) {
	            Imagen imagen = imagenServicio.crearImagen(imageModel);
	            if (imagen != null) {
	                imagen.setPublicacion(publicacionNueva); // Establecer la relación
	                listaDeImagenes.add(imagen);
	            }
	        }
	    }

	    publicacionNueva.setImagenes(listaDeImagenes);
	    
	    // Guardar la publicación
	    Publicacion savedPublicacion = publicacionRepositorio.save(publicacionNueva);
	    
	    return savedPublicacion;
	}

	
	public List<Publicacion> obtenerPublicaciones() {
		return publicacionRepositorio.findAll();
	}
	
	public List<Publicacion> obtenerPublicacionesActivas() {
		return publicacionRepositorio.findAllByDeletedFalse();
	}
	
	public Optional<Publicacion> buscarPublicacionPorId(Long id) {
		return publicacionRepositorio.findById(id);
	}
	

    public void incrementarVisualizaciones(Publicacion publicacion) {
        publicacion.setCantidadDeVisualizaciones(publicacion.getCantidadDeVisualizaciones() + 1);
        publicacionRepositorio.save(publicacion);
    }
	
	public boolean cambiarEstado(Long id) {
		Optional <Publicacion> opcPublicacion = buscarPublicacionPorId(id);
		
		if (opcPublicacion.isPresent()) {
			Publicacion publicacion = opcPublicacion.get();
			publicacion.setDeleted(!publicacion.isDeleted());
			publicacionRepositorio.save(publicacion);
			
			return true;
		} 
		return false;
	}
	
	public Publicacion editarPublicacion(Long publicacionId, PublicacionDto publicacionEditada) throws Exception{
		Publicacion publicacionDB = publicacionRepositorio.findById(publicacionId)
			.orElseThrow(() -> new Exception("Publicacion no encontrada"));
		
		if(publicacionEditada.getTitulo() != null) publicacionDB.setTitulo(publicacionEditada.getTitulo());
		if(publicacionEditada.getDescripcion() != null) publicacionDB.setDescripcion(publicacionEditada.getDescripcion());
		if(publicacionEditada.getFechaDeCreacion() != null) publicacionDB.setFechaDeCreacion(publicacionEditada.getFechaDeCreacion());
		if(publicacionEditada.getUsuarioCreador() != null) publicacionDB.setUsuarioCreador(publicacionEditada.getUsuarioCreador());
		
		publicacionRepositorio.save(publicacionDB);
		
		return publicacionDB;
		
	}
	
	
	public boolean borrarPublicacion(Long id) {
		Optional <Publicacion> opcPublicacion = buscarPublicacionPorId(id);
		
		if (opcPublicacion.isPresent()) {
			Publicacion borrarPublicacion = opcPublicacion.get();
			borrarPublicacion.setDeleted(true);
			publicacionRepositorio.save(borrarPublicacion);
			
			return true;
		}else {
			return false;
		}	
		
	}

	
	
	
	
}
