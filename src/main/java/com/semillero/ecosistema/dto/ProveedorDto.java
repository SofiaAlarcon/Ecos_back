package com.semillero.ecosistema.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProveedorDto {

	public enum EstadoProveedorDTO {
        REVISION_INICIAL,
        ACEPTADO,
        DENEGADO,
        REQUIERE_CAMBIOS,
        CAMBIOS_REALIZADOS
    }

    private Long id;

    private String nombre;

    private String descripcion;
    
    private String tipoProveedor;

    private String telefono;

    private String email;

    private String facebook;

    private String instagram;

    private String ciudad;

    private EstadoProveedorDTO estado;

    private Long paisId;

    private Long provinciaId;

    private Long categoriaId;

    private Long usuarioId;

    private String feedback;

    private boolean deleted = false;


}
