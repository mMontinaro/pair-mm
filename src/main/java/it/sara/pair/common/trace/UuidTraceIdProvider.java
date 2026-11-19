package it.sara.pair.common.trace;

public class UuidTraceIdProvider implements TraceIdProvider {
    @Override
    public String getCurrentTraceId() {
        return java.util.UUID.randomUUID().toString();
    }
    
}
