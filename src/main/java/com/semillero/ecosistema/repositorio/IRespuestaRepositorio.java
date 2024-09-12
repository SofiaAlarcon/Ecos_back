package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.semillero.ecosistema.entidad.Respuesta;

public interface IRespuestaRepositorio extends JpaRepository<Respuesta,Long> {

}
