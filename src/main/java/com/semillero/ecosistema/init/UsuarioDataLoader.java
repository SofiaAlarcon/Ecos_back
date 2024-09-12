package com.semillero.ecosistema.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Usuario.RolDeUsuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;

@Component
@Order(4)
public class UsuarioDataLoader implements CommandLineRunner {
		@Autowired
		IUsuarioRepositorio usuarioRepositorio;

		@Override
		public void run(String... args) throws Exception {
			loadUsuarioData();
		}
		
		private void loadUsuarioData() {
			if (usuarioRepositorio.count() == 0) {
				Usuario usuario1 = new Usuario(null, "Eco", "Sistema", "ecosistematt@gmail.com", false, RolDeUsuario.ADMIN );	
				usuarioRepositorio.save(usuario1);
			}
			
		}
}
