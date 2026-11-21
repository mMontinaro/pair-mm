package it.sara.pair.facades.validation;

import java.time.Clock;
import java.time.YearMonth;

import org.springframework.stereotype.Component;

import it.sara.pair.common.constants.Messages;
import it.sara.pair.common.constants.ValidationRules;
import it.sara.pair.common.dtos.BaseFilterDto;
import it.sara.pair.common.error.ValidationException;
import it.sara.pair.dto.ItemDetailDto;
import it.sara.pair.dto.ItemFieldDto;

@Component 
public class VaultItemValidator {
    private final Clock clock;

    public VaultItemValidator(){
        this(Clock.systemDefaultZone());
    }

    public VaultItemValidator(Clock clock){
        this.clock = clock;
    }

    public void validateWrite(ItemDetailDto dto){
        // Check for missing request
        require(dto != null, Messages.Validation.REQUEST_REQUIRED);

        // Check for required title
        require(notBlank(dto.title()), Messages.Validation.TITLE_REQUIRED);
        // Check for required type
        require(dto.type() != null, Messages.Validation.TYPE_REQUIRED);
        
        // *** EXTRA FIELDS ***
        validateFields(dto);

        switch(dto.type()){
            case WEB_ACCOUNT -> {
                // *** For WEB ***
                validateWebAccount(dto);
            }
            case PAYMENT_CARD -> {
                // *** For CARD ***
                validatePaymentCard(dto);
            }
            case PERSONAL_INFORMATION -> { }
            default -> {
                throw new ValidationException(Messages.VALIDATION_FAILED);
            }
        }
    }

    public void validateFilters(BaseFilterDto filter){
        require(filter != null, Messages.Validation.REQUEST_REQUIRED);
        require(
            filter.offset() >= 0
            && filter.pageSize() >= ValidationRules.MIN_SEARCH_PAGE_SIZE
            && filter.pageSize() <= ValidationRules.MAX_SEARCH_PAGE_SIZE
            && filter.order() != null, Messages.VALIDATION_FAILED);
        
    }

    private void validateWebAccount(ItemDetailDto dto){
        // Check for required username
        require(notBlank(dto.username()), Messages.Validation.USERNAME_REQUIRED);
        // Check for required password
        require(notBlank(dto.password()), Messages.Validation.PASSWORD_REQUIRED);
    }
    
    private void validatePaymentCard(ItemDetailDto dto){
        // Check for required cardholder
        require(notBlank(dto.cardholder()), Messages.Validation.CARDHOLDER_NAME_REQUIRED);
        require(validCardNumber(dto.cardNumber()), Messages.Validation.CARD_NUMBER_NOT_VALID);
        // Check for CVV regex not required according to requisites, want to save time on this
        // TODO remove requirement of cvv
        require(notBlank(dto.cvv()) && dto.cvv().matches(ValidationRules.CVV_REGEX), Messages.Validation.CVV_NOT_VALID);
        // TODO remove requirement of expiry date
        require(notBlank(dto.expiry()) && validateExpiry(dto.expiry()), Messages.Validation.EXPIRY_DATE_NOT_VALID);
    }
    
    private void validateFields(ItemDetailDto dto){
        // if extra fields, check for empty list
        if(dto.fields() != null){
            require(!dto.fields().isEmpty(), Messages.VALIDATION_FAILED);
            // if extra fields, check for key required in each field
            for(ItemFieldDto field : dto.fields()){
                require(field != null && notBlank(field.key()), Messages.VALIDATION_FAILED);
            }
        }
    }
    
    private boolean validCardNumber(String cardNumber){
        // Check for required cardNumber
        // Check for cardNumber regex
        // Check for cardNumber Luhn checksum
        return notBlank(cardNumber) && passesLuhnChecksum(cardNumber);
    }
    
    private boolean passesLuhnChecksum(String cardNumber){
        if(!cardNumber.matches(ValidationRules.CARD_NUMBER_REGEX)){
            return false;
        }
        int sum = 0;
        boolean doubleDigit = false;
        for(int i = cardNumber.length() -1; i >= 0; i--){
            int digit = cardNumber.charAt(i) - '0';
            if(doubleDigit){
                digit *= 2;
                if(digit > 9 ){
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }

    private boolean validateExpiry(String expiry){
        // Check for expiry regex
        if(!expiry.matches(ValidationRules.EXPIRY_DATE_REGEX)){
            return false;
        }
        // Check for expiry date not in past
        int month = Integer.parseInt(expiry.substring(0,2));
        int year = 2000 + Integer.parseInt(expiry.substring(3));
        if(month < 1 || month > 12){
            return false;
        }
        return !YearMonth.of(year, month).isBefore(YearMonth.now(clock));
    }

    private void require(boolean valid, String errorMessage){
        if(!valid){
            throw new ValidationException(errorMessage);
        }
    }

    private boolean notBlank(String value){
        return value != null && !value.isBlank();
    }
}
