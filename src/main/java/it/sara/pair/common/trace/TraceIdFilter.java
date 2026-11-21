package it.sara.pair.common.trace;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import it.sara.pair.common.constants.General;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString();

        try {
            MDC.put(General.TRACE_ID, traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(General.TRACE_ID);
        }
    }
}
