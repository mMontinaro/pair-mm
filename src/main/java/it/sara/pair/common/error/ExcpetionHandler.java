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
import it.sara.pair.common.trace.TraceIdProvider;

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
    private final TraceIdProvider traceIds;

    public ExcpetionHandler(TraceIdProvider traceIds) {
        this.traceIds = traceIds;
    }

    private ResponseEntity<BaseResponse<Void>> response(int code, String message) {
        return ResponseEntity.ok(
            new BaseResponse<>(
                new BaseStatus(
                    code, 
                    message, 
                    traceIds.getCurrentTraceId()
                ), 
                null
            ));
    }

    @ExceptionHandler (BaseException.class)
    public ResponseEntity<BaseResponse<Void>> business(BaseException e) {
        return response(e.getCode(), e.getMessage());
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> business(MethodArgumentNotValidException e) {
        return response(HttpStatus.BAD_REQUEST.value(), Messages.VALIDATION_FAILED);
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<BaseResponse<Void>> unexpected(Exception e) {
        LOG.error(Messages.UNEXPECTED_ERROR, e);
        return response(HttpStatus.INTERNAL_SERVER_ERROR.value(), Messages.UNEXPECTED_ERROR);
    }

}
