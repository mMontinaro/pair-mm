package it.sara.pair.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.sara.pair.enums.ItemType;

@JsonInclude (JsonInclude.Include.NON_NULL)
public record ItemDetailDto(
    UUID id,
    ItemType type,
    String title,
    String url,
    String username,
    String password,
    String cardholder,
    String cardNumber,
    String cvv,
    String expiry,
    List<ItemFieldDto> fields
) {}