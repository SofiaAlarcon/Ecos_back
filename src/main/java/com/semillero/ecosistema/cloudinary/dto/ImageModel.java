package com.semillero.ecosistema.cloudinary.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ImageModel {

    private String nombre;
    private MultipartFile file;
}

