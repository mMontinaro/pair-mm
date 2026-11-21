package it.sara.pair.common.factory;

import org.springframework.http.HttpStatus;

import it.sara.pair.common.api.BaseStatus;
import it.sara.pair.common.constants.Messages;
import it.sara.pair.common.trace.TraceIdRetriever;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public class BaseStatusFactory {
    private final TraceIdRetriever retriever;

    public BaseStatus success(){
        return new BaseStatus(
            HttpStatus.OK.value(), 
            Messages.SUCCESS, 
            retriever.getCurrentTraceId()
        );
    }

    public BaseStatus error(int code, String message){
        return new BaseStatus(
            code, 
            message, 
            retriever.getCurrentTraceId()
        );
    }
}
