package it.sara.pair.common.dtos;

import it.sara.pair.common.enums.SearchOrder;

public record BaseFilterDto(
    String search, 
    int offset, 
    int pageSize,
    SearchOrder order
) {
    
}
