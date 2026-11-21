package it.sara.pair.dto;

public record ItemFieldDto(
    String key, 
    String value, 
    boolean isSensitive
) {}
