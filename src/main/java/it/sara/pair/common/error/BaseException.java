package it.sara.pair.common.error;

/**
 * BaseException base abstract class for client exceptions, all exceptions should extend this class
 */
public abstract class BaseException extends RuntimeException {
    private final int code;
    private final String extra;

    protected BaseException(int code, String message, String extra) {
        super(message);
        this.code = code;
        this.extra = extra;
    }
    
    protected BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.extra = null;
    }

    public int getCode() {
        return code;
    }

    public String getExtra() {
        return extra;
    }
}
