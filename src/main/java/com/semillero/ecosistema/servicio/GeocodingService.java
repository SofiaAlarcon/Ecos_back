package com.semillero.ecosistema.servicio;

import org.springframework.stereotype.Service;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageResponse;

@Service
public class GeocodingService {
	private String apiKey = "04a3964a4b7d41a68aeb99ab2ea205a5";
	
	private JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKey);
	
	
	public JOpenCageResponse doForwardRequest(String query) {
		JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
		
		JOpenCageResponse response = jOpenCageGeocoder.forward(request);
		
		return response;
	}
	

	
	
	
}
