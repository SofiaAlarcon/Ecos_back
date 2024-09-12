package com.semillero.ecosistema.dto.comparator;

import java.util.Comparator;

import com.semillero.ecosistema.dto.DistanciaDto;

public class DistanciaDtoComparator implements Comparator<DistanciaDto> {
	@Override
    public int compare(DistanciaDto a, DistanciaDto b) {
        return (int) ((int) a.getDistancia() - b.getDistancia());
    }
}
