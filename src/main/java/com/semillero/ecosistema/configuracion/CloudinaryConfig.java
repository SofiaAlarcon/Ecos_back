package com.semillero.ecosistema.configuracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;



@Configuration
public class CloudinaryConfig {

	@Value("${cloudinary.cloud_name:dyb7cwvgz}")
	private String cloudName;
	
	@Value("${cloudinary.api_key:599862417877693}")
	private String apiKey;
	
	@Value("${cloudinary.api_secret:aCT0x1D4mJVmteBomxwATcixv9k}")
	private String apiSecret;
	
	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret));
	}
	
}
