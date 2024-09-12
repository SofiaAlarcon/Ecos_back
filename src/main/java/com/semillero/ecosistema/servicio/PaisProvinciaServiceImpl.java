package com.semillero.ecosistema.servicio;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.dto.PaisDto;
import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.entidad.Provincia;
import com.semillero.ecosistema.repositorio.IPaisRepositorio;
import com.semillero.ecosistema.repositorio.IProvinciaRepositorio;

@Service
public class PaisProvinciaServiceImpl {

	@Autowired
	private IPaisRepositorio paisRepositorio;
	
	@Autowired
	private IProvinciaRepositorio provinciaRepositorio;
	
	public List<PaisDto> mostrarTodo() {
        return paisRepositorio.findAll().stream()
                .map(this::convertirAPaisDto)
                .collect(Collectors.toList());
    }
	
	private PaisDto convertirAPaisDto(Pais pais) {
		PaisDto dto=new PaisDto();
		dto.setId(pais.getId());
		dto.setNombre(pais.getNombre());
		return dto;
	}
	
	public PaisDto obtenerPaisDtoPorId(Long paisId) throws Exception {
	    Pais pais = paisRepositorio.findById(paisId).orElseThrow(() -> new Exception("País no encontrado"));
	    return convertirAPaisDto(pais);
	}
	
	public List<Provincia> mostrarProvinciasPorPaisId(Long paisId) {
	    Optional<Pais> opc = paisRepositorio.findById(paisId);
	    return opc.map(Pais::getProvincias).orElse(Collections.emptyList());
	}
	
	public Provincia mostrarProvinciaPorId(Long paisId, Long provinciaId) {
	    return provinciaRepositorio.findByIdAndPaisId(provinciaId, paisId).orElse(null);
	}

}
