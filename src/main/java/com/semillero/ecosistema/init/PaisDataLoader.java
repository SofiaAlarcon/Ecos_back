package com.semillero.ecosistema.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.repositorio.IPaisRepositorio;

@Component
@Order(2)
public class PaisDataLoader implements CommandLineRunner {

    @Autowired
    private IPaisRepositorio paisRepositorio;

    @Override
    public void run(String... args) throws Exception {
        loadPaisData();
    }

    private void loadPaisData() {
        if (paisRepositorio.count() == 0) {
            Pais[] paises = {
                new Pais(null, "Argentina"),
                new Pais(null, "Bolivia")
            };

            for (Pais pais : paises) {
                paisRepositorio.save(pais);
            }
        }
    }
}