package com.semillero.ecosistema.configuracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;



@Configuration
public class CloudinaryConfig {

	@Value("${cloudinary.cloud_name:NOT_CONFIGURED}")
	private String cloudName;
	
	@Value("${cloudinary.api_key:NOT_CONFIGURED}")
	private String apiKey;
	
	@Value("${cloudinary.api_secret:NOT_CONFIGURED}")
	private String apiSecret;
	
	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret));
	}
	
}
