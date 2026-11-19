package it.sara.pair.common.api;

import java.util.Objects;

/**
 * BaseStatus
 * @param code HTTP code, business, outcome, or error code
 * @param message human readable message, predefined meesage or underlying error description
 * @param traceId always populated, correlation id for logs
 */
public record BaseStatus(int code, String message, String traceId) {
    public BaseStatus {
        Objects.requireNonNull(code);
        Objects.requireNonNull(traceId);
    }
}
