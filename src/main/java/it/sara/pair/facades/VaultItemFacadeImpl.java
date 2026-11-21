package it.sara.pair.facades;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.sara.pair.common.api.BaseStatus;
import it.sara.pair.common.api.PagedResponse;
import it.sara.pair.common.api.PagedResult;
import it.sara.pair.common.constants.Messages;
import it.sara.pair.common.dtos.BaseFilterDto;
import it.sara.pair.common.enums.SearchOrder;
import it.sara.pair.common.facade.BaseFacadeImpl;
import it.sara.pair.common.security.VaultSecretService;
import it.sara.pair.dto.ItemDetailDto;
import it.sara.pair.dto.ItemFieldDto;
import it.sara.pair.enums.ItemType;
import it.sara.pair.facades.validation.VaultItemValidator;
import it.sara.pair.model.ItemField;
import it.sara.pair.model.PaymentCardItem;
import it.sara.pair.model.PersonalInformationItem;
import it.sara.pair.model.VaultItem;
import it.sara.pair.model.WebAccountItem;
import it.sara.pair.services.VaultItemService;
import jakarta.validation.ValidationException;

@Component
@Transactional(readOnly = true)
public class VaultItemFacadeImpl extends BaseFacadeImpl<ItemDetailDto, UUID> implements VaultItemFacade {

    private final VaultItemService service;
    private final VaultSecretService secret;
    private final VaultItemValidator validator;

    public VaultItemFacadeImpl(VaultItemService service, VaultSecretService secret, VaultItemValidator validator) {
        this.service = service;
        this.secret = secret;
        this.validator = validator;
    }

    @Override
    @Transactional
    public ItemDetailDto create(ItemDetailDto dto) {
        validator.validateWrite(requireRequest(dto));
        VaultItem item = newEntity(dto.type());
        apply(item, dto);
        return toDto(service.create(item), false);
    }

    @Override
    public ItemDetailDto detail(UUID id) {
        return toDto(service.detail(id), true);
    }

    @Override
    @Transactional 
    public ItemDetailDto update(UUID id, ItemDetailDto dto) {
        validator.validateWrite(dto);
        VaultItem entity = service.detail(id);
        if(entity.getItemType() != dto.type()){
            throw new ValidationException(Messages.VALIDATION_FAILED);
        }
        apply(entity, dto);
        return toDto(service.update(entity), false);
    }

    @Override
    @Transactional 
    public ItemDetailDto delete(UUID id) {
        return toDto(service.delete(id), false);
    }

    @Override
    public PagedResult<ItemDetailDto> search(BaseFilterDto filter) {
        validator.validateFilters(filter);
        Sort.Direction direction = filter.order() == SearchOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<VaultItem> page = service.search(filter.search(), filter.offset(), filter.pageSize(), direction);
        List<ItemDetailDto> list = page.getContent().stream().map(
            item -> new ItemDetailDto(item.getId(), null, item.getTitle(), item.getUrl(), null, null, null, null, null, null, null)
        ).toList();
        return new PagedResult<ItemDetailDto>(page.getTotalElements(), list);
    }

    private VaultItem newEntity(ItemType type) {
        return switch (type) {
            case WEB_ACCOUNT -> new WebAccountItem();
            case PERSONAL_INFORMATION -> new PersonalInformationItem();
            case PAYMENT_CARD -> new PaymentCardItem();
        };
    }

    private void apply(VaultItem item, ItemDetailDto dto){
        item.setTitle(dto.title());
        item.setUrl(dto.url());

        if(item instanceof WebAccountItem web){
            web.setUsername(dto.username());
            web.setPassword(secret.protect(dto.password()));
        } else if(item instanceof PaymentCardItem card){
            card.setCardHolder(dto.cardholder());
            card.setCardNumber(secret.protect(dto.cardNumber()));
            card.setCvv(secret.protect(dto.cvv()));
            card.setExpirationDate(dto.expiry());
        }

        for(ItemField old : new ArrayList<>(item.getFields())){
            item.getFields().remove(old);
            old.setVaultItem(null);
        }
        if(dto.fields() != null){
            for(ItemFieldDto field : dto.fields()){
                item.addField(new ItemField(
                    field.key(), 
                    secret.storeFieldValue(field.value(), field.isSensitive()),
                    field.isSensitive(),
                    item));
            }
        }
    }

    private ItemDetailDto toDto(VaultItem item, boolean isDetail){
        String username = null;
        String password = null;
        String cardholder = null;
        String cardNumber = null;
        String cvv = null;
        String expiry = null;

        // At the moment all sensitive attributes are returned as ciphertext
        // Not the most elegant way of returning an encrypted password. maybe a bunch of *** would suffice
        // At the time of coding this I don't recall how I view passwords, or how it might facilitate the frontend to visualize.
        // TODO For future iterations, check if sensitive data can be passed by *, null, or ciphertext
        if(item instanceof WebAccountItem web){
            username = web.getUsername();
            password = isDetail ? secret.reveal(web.getPassword()) : web.getPassword();
        } else if(item instanceof PaymentCardItem card){
            cardholder = card.getCardHolder();
            cardNumber = isDetail ? secret.reveal(card.getCardNumber()) : card.getCardNumber();
            cvv = isDetail ? secret.reveal(card.getCvv()): card.getCvv();
            expiry = card.getExpirationDate();
        }

        List<ItemFieldDto> fields = item.getFields().stream()
            .map(field -> new ItemFieldDto(
                field.getKey(),
                isDetail ? secret.readFieldValue(field.getValue(), field.isSensitive()) : field.getValue(),
                field.isSensitive())
            ).toList();
        
        return new ItemDetailDto(
            item.getId(),
            item.getItemType(),
            item.getTitle(),
            item.getUrl(),
            username,
            password,
            cardholder,
            cardNumber,
            cvv,
            expiry,
            fields
        );
    }
}
