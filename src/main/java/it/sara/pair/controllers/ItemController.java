package it.sara.pair.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.sara.pair.common.api.BaseResponse;
import it.sara.pair.common.api.PagedResponse;
import it.sara.pair.common.api.PagedResult;
import it.sara.pair.common.dtos.BaseFilterDto;
import it.sara.pair.common.factory.BaseStatusFactory;
import it.sara.pair.dto.ItemDetailDto;
import it.sara.pair.facades.VaultItemFacade;
import jakarta.validation.Valid;

@RestController 
@RequestMapping ("item/v1/item")
public class ItemController { 
    private static final String BASE_PATH = "item/v1/item"; 

    private final VaultItemFacade facade;
    private final BaseStatusFactory factory;
    
    public ItemController(VaultItemFacade facade, BaseStatusFactory factory){
        this.facade = facade;
        this.factory = factory;
    }   

    @PutMapping(BASE_PATH)
    public ResponseEntity<BaseResponse<ItemDetailDto>> create(@Valid @RequestBody ItemDetailDto dto) {
        ItemDetailDto result = facade.create(dto);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), result)
        );
    }

    @PostMapping(BASE_PATH + "/{guid}")
    public ResponseEntity<BaseResponse<ItemDetailDto>> update(@PathVariable UUID guid, @Valid @RequestBody ItemDetailDto dto) {
        ItemDetailDto result = facade.update(guid, dto);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), result)
        );    
    }

    @DeleteMapping (BASE_PATH + "/{guid}")
    public ResponseEntity<BaseResponse<UUID>> delete(@PathVariable UUID guid) {
        facade.delete(guid);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), guid)
        );    
    }

    @PostMapping(BASE_PATH + "/search")
    public ResponseEntity<PagedResponse<ItemDetailDto>> search(@Valid @RequestBody BaseFilterDto filter) {
        PagedResult<ItemDetailDto> result = facade.search(filter);
        
        return ResponseEntity.ok(
            new PagedResponse<>(
                factory.success(), 
                result.total(),
                result.data()
            )
        );    
    }

    @GetMapping(BASE_PATH + "/{guid}")
    public ResponseEntity<BaseResponse<ItemDetailDto>> detail(@PathVariable UUID guid) {
        ItemDetailDto result = facade.detail(guid);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), result)
        );    
    }
}