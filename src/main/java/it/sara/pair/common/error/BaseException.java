package it.sara.pair.common.error;

/**
 * BaseException base abstract class for client exceptions, all exceptions should extend this class
 */
public abstract class BaseException extends RuntimeException {
    private final int code;

    protected BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
