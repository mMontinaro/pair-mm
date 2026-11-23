package it.sara.pair.common.trace;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import it.sara.pair.common.constants.General;

@Component
public class TraceIdRetriever implements TraceIdProvider {
    @Override
    public String getCurrentTraceId() {
        return MDC.get(General.TRACE_ID);
    }
    
}
