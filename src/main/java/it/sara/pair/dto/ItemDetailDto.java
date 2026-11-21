package it.sara.pair.dto;

import java.util.List;
import java.util.UUID;

import it.sara.pair.enums.ItemType;

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