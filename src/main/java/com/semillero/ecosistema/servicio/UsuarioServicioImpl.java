package com.semillero.ecosistema.servicio;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;

@Service
public class UsuarioServicioImpl  {
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;

	@Transactional
	public Usuario guardar(Usuario usuario) {
		return usuarioRepositorio.save(usuario);
	}

	@Transactional
	public boolean desactivarUsuario(Long id) {
	    Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(id);
	    if (usuarioOpt.isPresent()) {
	        Usuario usuario = usuarioOpt.get();
	        if (!usuario.isDeleted()) {
	            return false; // Ya está desactivado
	        }
	        usuario.setDeleted(false);
	        usuarioRepositorio.save(usuario);
	        return true;
	    }
	    return false;
	}
	
	@Transactional
	public Usuario buscarPorId(Long id) {
		
		Optional<Usuario> opc=usuarioRepositorio.findById(id);
		return  opc.orElse(null);
	}
	
	public Usuario authenticateUser(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByEmail(email);
        return usuarioOpt.orElse(null);
	}
}