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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.sara.pair.common.api.BaseResponse;
import it.sara.pair.common.api.PagedResponse;
import it.sara.pair.common.api.PagedResult;
import it.sara.pair.common.dtos.BaseFilterDto;
import it.sara.pair.common.factory.BaseStatusFactory;
import it.sara.pair.dto.ItemDetailDto;
import it.sara.pair.facades.VaultItemFacade;
import jakarta.validation.Valid;

@Tag (
    name = "Vault Items",
    description = "Manage web accounts, personal information, and payment cards."
)
@RestController 
@RequestMapping ("item/v1/item")
public class ItemController { 

    private final VaultItemFacade facade;
    private final BaseStatusFactory factory;
    
    public ItemController(VaultItemFacade facade, BaseStatusFactory factory){
        this.facade = facade;
        this.factory = factory;
    }   


    @Operation(
        summary = "Create a vault item",
        description = """
            Creates a WEB_ACCOUNT, PERSONAL_INFORMATION, or PAYMENT_CARD item.
            Sensitive values are encrypted before persistence.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "For Outcome code - status.code"
        )
    })
    @PutMapping
    public ResponseEntity<BaseResponse<ItemDetailDto>> create(@Valid @RequestBody ItemDetailDto dto) {
        ItemDetailDto result = facade.create(dto);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), result)
        );
    }


    @Operation(
        summary = "Update a vault item",
        description = """
            Updates an existing vault item identified by GUID.
            Sensitive values are encrypted before persistence.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "For Outcome code - status.code"
        )
    })    
    @PostMapping("/{guid}")
    public ResponseEntity<BaseResponse<ItemDetailDto>> update(@PathVariable UUID guid, @Valid @RequestBody ItemDetailDto dto) {
        ItemDetailDto result = facade.update(guid, dto);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), result)
        );    
    }


    @Operation(
        summary = "Delete a vault item",
        description = """
            Deletes a vault item identified by GUID, including its associated fields.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "For Outcome code - status.code"
        )
    })   
    @DeleteMapping ("/{guid}")
    public ResponseEntity<BaseResponse<UUID>> delete(@PathVariable UUID guid) {
        facade.delete(guid);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), guid)
        );    
    }


    @Operation(
        summary = "Search vault items",
        description = """
            Searches vault items by title or URL, with offset-based pagination
            and sorting. Maximum page size is 20.

            Results contain only the item identifier, type, title, and optional URL.
            Passwords, card numbers, CVVs, and custom fields are excluded.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "For Outcome code - status.code"
        )
    })
    @PostMapping("/search")
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


    @Operation(
        summary = "Retrieve vault item details",
        description = """
            Retrieves a vault item identified by GUID, including its type-specific
            fields and decrypted sensitive values.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "For Outcome code - status.code"
        )
    })
    @GetMapping("/{guid}")
    public ResponseEntity<BaseResponse<ItemDetailDto>> detail(@PathVariable UUID guid) {
        ItemDetailDto result = facade.detail(guid);

        return ResponseEntity.ok(
            new BaseResponse<>(factory.success(), result)
        );    
    }
}