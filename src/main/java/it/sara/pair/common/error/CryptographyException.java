package it.sara.pair.common.error;

import org.springframework.http.HttpStatus;

public class CryptographyException extends BaseException {
    public CryptographyException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
