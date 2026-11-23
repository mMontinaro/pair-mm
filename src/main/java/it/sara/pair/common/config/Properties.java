package it.sara.pair.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pair.api") 
public record Properties (String title, String description, String version){}
