package com.semillero.ecosistema.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Proveedor;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailCuerpoServicio {

	@Autowired
	private JavaMailSender enviarEmail;
	
	public String generarCuerpoEmail(List<Proveedor> proveedores) {
	    StringBuilder cuerpo = new StringBuilder();
	    cuerpo.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px; background-color: #f9f9f9;'>");
	    cuerpo.append("<h2 style='color: #4CAF50; text-align: center;'>Nuevos Proveedores Aceptados Esta Semana</h2>");
	    cuerpo.append("<p style='text-align: center; color: #555;'>A continuación, encontrarás los detalles de los proveedores que han sido aceptados recientemente:</p>");
	    cuerpo.append("<ul style='list-style-type: none; padding: 0;'>");

	    for (Proveedor proveedor : proveedores) {
	        cuerpo.append("<li style='background-color: #fff; border: 1px solid #ddd; margin-bottom: 10px; padding: 10px; border-radius: 5px;'>");
	        cuerpo.append("<h3 style='color: #333;'><strong>Nombre:</strong>" + proveedor.getNombre() + "</h3>");
	        cuerpo.append("<p style='margin: 0; color: #777;'><strong>Descripción:</strong> " + proveedor.getDescripcion() + "</p>");
	        cuerpo.append("<p style='margin: 0; color: #777;'><strong>Tipo:</strong> " + proveedor.getTipoProveedor() + "</p>");
	        cuerpo.append("<p style='margin: 0; color: #777;'><strong>Ciudad:</strong> " + proveedor.getCiudad() + "</p>");
	        cuerpo.append("<p style='margin: 0; color: #777;'><strong>Categoría:</strong> " + proveedor.getCategoria().getNombre() + "</p>");
	        cuerpo.append("</li>");
	    }

	    cuerpo.append("</ul>");
	    cuerpo.append("<p style='text-align: center; color: #555;'>Gracias por su atención.</p>");
	    cuerpo.append("</div>");
	    return cuerpo.toString();
	}
	
	public void enviarCorreo(String to,String subject, String body) {
		MimeMessage mensaje=enviarEmail.createMimeMessage();
		try {
			MimeMessageHelper ayuda = new MimeMessageHelper(mensaje,true);
			ayuda.setTo(to);
			ayuda.setSubject(subject);
			ayuda.setText(body,true);
			enviarEmail.send(mensaje);
		}catch(MessagingException e){
			e.printStackTrace();
		}
	}
}
