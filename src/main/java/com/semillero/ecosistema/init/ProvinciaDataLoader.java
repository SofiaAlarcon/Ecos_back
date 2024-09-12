package com.semillero.ecosistema.init;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.entidad.Provincia;
import com.semillero.ecosistema.repositorio.IPaisRepositorio;
import com.semillero.ecosistema.repositorio.IProvinciaRepositorio;

@Component
@Order(3)
public class ProvinciaDataLoader implements CommandLineRunner {

    @Autowired
    private IProvinciaRepositorio provinciaRepositorio;

    @Autowired
    private IPaisRepositorio paisRepositorio;

    @Override
    public void run(String... args) throws Exception {
        loadProvinciaData();
    }

    private void loadProvinciaData() {
        if (provinciaRepositorio.count() == 0) {
            Optional<Pais> argentina = paisRepositorio.findById(1L);
            Optional<Pais> bolivia = paisRepositorio.findById(2L);

            if (argentina.isPresent() && bolivia.isPresent()) {
                Provincia[] provinciasArgentina = {
                    new Provincia(null, "Buenos Aires", argentina.get()),
                    new Provincia(null, "Córdoba", argentina.get()),
                    new Provincia(null, "Santa Fe", argentina.get()),
                    new Provincia(null, "Mendoza", argentina.get()),
                    new Provincia(null, "Tucumán", argentina.get()),
                    new Provincia(null, "Entre Ríos", argentina.get()),
                    new Provincia(null, "Salta", argentina.get()),
                    new Provincia(null, "Misiones", argentina.get()),
                    new Provincia(null, "Chaco", argentina.get()),
                    new Provincia(null, "Corrientes", argentina.get()),
                    new Provincia(null, "Santiago del Estero", argentina.get()),
                    new Provincia(null, "San Juan", argentina.get()),
                    new Provincia(null, "Jujuy", argentina.get()),
                    new Provincia(null, "Río Negro", argentina.get()),
                    new Provincia(null, "Formosa", argentina.get()),
                    new Provincia(null, "Neuquén", argentina.get()),
                    new Provincia(null, "San Luis", argentina.get()),
                    new Provincia(null, "Catamarca", argentina.get()),
                    new Provincia(null, "La Rioja", argentina.get()),
                    new Provincia(null, "La Pampa", argentina.get()),
                    new Provincia(null, "Santa Cruz", argentina.get()),
                    new Provincia(null, "Tierra del Fuego", argentina.get())
                };

                Provincia[] provinciasBolivia = {
                    new Provincia(null, "La Paz", bolivia.get()),
                    new Provincia(null, "Santa Cruz", bolivia.get()),
                    new Provincia(null, "Cochabamba", bolivia.get()),
                    new Provincia(null, "Oruro", bolivia.get()),
                    new Provincia(null, "Potosí", bolivia.get()),
                    new Provincia(null, "Chuquisaca", bolivia.get()),
                    new Provincia(null, "Tarija", bolivia.get()),
                    new Provincia(null, "Beni", bolivia.get()),
                    new Provincia(null, "Pando", bolivia.get())
                };

                for (Provincia provincia : provinciasArgentina) {
                    provinciaRepositorio.save(provincia);
                }

                for (Provincia provincia : provinciasBolivia) {
                    provinciaRepositorio.save(provincia);
                }
            }
        }
    }
}