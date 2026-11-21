package it.sara.pair.common.trace;

import org.slf4j.MDC;

import it.sara.pair.common.constants.General;

public class TraceIdRetriever implements TraceIdProvider {
    @Override
    public String getCurrentTraceId() {
        return MDC.get(General.TRACE_ID);
    }
    
}
