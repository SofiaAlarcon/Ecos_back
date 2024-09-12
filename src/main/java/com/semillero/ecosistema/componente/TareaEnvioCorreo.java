package com.semillero.ecosistema.componente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.servicio.ProveedorServicio;

@Component
public class TareaEnvioCorreo {
	
	@Autowired
	private ProveedorServicio proveedorServicio;
	
	//para probar que funcioone usar "0 * * * * MON-FRI"
	@Scheduled(cron = "0 0 9 * * MON") 
	public void enviarReporteSemanal() {
		System.out.println("Iniciando tarea programada de envío de reporte semanal.");
		proveedorServicio.enviarReporteSemanal();
		System.out.println("Tarea programada de envío de reporte semanal finalizada.");
	}

}
