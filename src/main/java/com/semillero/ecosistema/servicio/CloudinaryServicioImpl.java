package com.semillero.ecosistema.servicio;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.semillero.ecosistema.dto.ImageUploadResult;

import jakarta.annotation.Resource;

@Service
public class CloudinaryServicioImpl {

    @Resource
    private Cloudinary cloudinary;

    public ImageUploadResult cargarImagen(MultipartFile file) {
        ImageUploadResult result = new ImageUploadResult();
        try {
            Map<String, Object> uploadedFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String publicId = (String) uploadedFile.get("public_id");
            String url = (String) uploadedFile.get("url");
            result.setPublicId(publicId);
            result.setUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public void eliminarImagen(String publicId) throws IOException {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error al eliminar la imagen con publicId: " + publicId, e);
        }
    }
}
