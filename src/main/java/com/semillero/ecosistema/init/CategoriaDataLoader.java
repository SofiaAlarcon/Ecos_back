package com.semillero.ecosistema.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.repositorio.ICategoriaRepositorio;

@Component
@Order(1)
public class CategoriaDataLoader implements CommandLineRunner {
	@Autowired
	private ICategoriaRepositorio categoriaRepositorio;

	@Override
	public void run(String... args) throws Exception {
		loadCategoriaData();
	}

	private void loadCategoriaData() {
		if (categoriaRepositorio.count() == 0) {
			Categoria[] categorias = {
					new Categoria(null, "Bienestar"),
					new Categoria(null, "Capacitaciones"),
					new Categoria(null, "Construcción"),
					new Categoria(null, "Cultivos"),
					new Categoria(null, "Gastronomía"),
					new Categoria(null, "Indumentaria"),
					new Categoria(null, "Merchandasing"),
					new Categoria(null, "Muebles/Deco"),
					new Categoria(null, "Reciclaje"),
					new Categoria(null, "Tecnología"),
					new Categoria(null, "Transporte")
			};

			for (Categoria categoria : categorias) {
				categoriaRepositorio.save(categoria);
			}
		}
	}
}

