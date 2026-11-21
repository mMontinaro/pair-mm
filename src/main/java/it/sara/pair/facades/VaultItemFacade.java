package it.sara.pair.facades;

import java.util.UUID;

import it.sara.pair.common.api.PagedResult;
import it.sara.pair.common.dtos.BaseFilterDto;
import it.sara.pair.common.facade.BaseFacade;
import it.sara.pair.dto.ItemDetailDto;

public interface VaultItemFacade extends BaseFacade<ItemDetailDto, UUID> {
    PagedResult<ItemDetailDto> search(BaseFilterDto filter);
    
}
