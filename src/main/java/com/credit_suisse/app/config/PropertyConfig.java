package com.credit_suisse.app.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


@Configuration
@PropertySource(name = "appProperties", value = {"classpath:app.properties"})
public class PropertyConfig {
    
	@Bean
	public static PropertyPlaceholderConfigurer propertyConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();		
		Resource[] resources = new ClassPathResource[] { new ClassPathResource( "app.properties" ) };
		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		return ppc;
	}
	
}
