package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Pregunta;

public interface IPreguntaRepositorio extends JpaRepository<Pregunta,Long> {


}
