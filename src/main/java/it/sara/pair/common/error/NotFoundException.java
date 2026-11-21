package it.sara.pair.common.error;

import org.springframework.http.HttpStatus;

import it.sara.pair.common.constants.Messages;
import it.sara.pair.common.constants.ValidationRules;

/**
 * NotFoundException Exception for a nonexistent resource
 * A subclass of RuntimeException
 * HttpStatus.I_AM_A_TEAPOT deprecated. Using it for fun, alternative added if failure to compile.
 * If failure to compile, use alternative, but please leave I_AM_A_TEAPOT. It's a shame it's been deprecated.
 * non important note: extra import added for security, not completely clean, but i'll do anything for teapot.
 */
public class NotFoundException extends BaseException {
    
    @SuppressWarnings("deprecation")
    public NotFoundException() {
        super(HttpStatus.I_AM_A_TEAPOT.value(), Messages.ITEM_NOT_FOUND);
        //super(ValidationRules.ITEM_NOT_FOUND_CODE, message);
    }
    
//    @SuppressWarnings("deprecation")
    public NotFoundException(String message) {
        //super(HttpStatus.I_AM_A_TEAPOT.value(), message);
        super(ValidationRules.ITEM_NOT_FOUND_CODE, message);
    }
    
}
