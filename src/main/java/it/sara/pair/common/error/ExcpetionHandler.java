package it.sara.pair.common.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import it.sara.pair.common.api.BaseResponse;
import it.sara.pair.common.api.BaseStatus;
import it.sara.pair.common.constants.Messages;
import it.sara.pair.common.factory.BaseStatusFactory;

/**
 * ExcpetionHandler
 * Central exception handling class.
 * All methods invoke response, which always guarantees HTTP 200. 
 * Real HTTP code and message are inside BaseResponse -> BaseStatus -> code + message.
 * Handles generic exceptions, runtime exceptions, and validation exceptions.
 */
@RestControllerAdvice 
public class ExcpetionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExcpetionHandler.class);
    private final BaseStatusFactory factory;

    public ExcpetionHandler(BaseStatusFactory factory) {
        this.factory = factory;
    }

    private ResponseEntity<BaseResponse<Void>> response(int code, String message, String error) {
        BaseStatus status = factory.error(code, message);
        LOG.error(
            "traceId={} --- code={} --- message={} --- error={}",
            status.traceId(),
            code,
            message,
            error
        );
        return ResponseEntity.ok(
            new BaseResponse<>(
                status, 
                null
            ));
    }

    @ExceptionHandler (BaseException.class)
    public ResponseEntity<BaseResponse<Void>> business(BaseException e) {
        return response(e.getCode(), e.getMessage(), e.getExtra());
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> business(MethodArgumentNotValidException e) {
        return response(HttpStatus.BAD_REQUEST.value(), Messages.VALIDATION_FAILED, e.getMessage());
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<BaseResponse<Void>> unexpected(Exception e) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR.value(), Messages.UNEXPECTED_ERROR, e.getMessage());
    }

}
