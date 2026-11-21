package it.sara.pair.common.facade;

import it.sara.pair.common.constants.Messages;
import it.sara.pair.common.error.ValidationException;

public abstract class BaseFacadeImpl<C, ID> implements BaseFacade<C, ID> {
    protected final C requireRequest(C request) {
        if (request == null) {
            throw new ValidationException(
                    Messages.Validation.REQUEST_REQUIRED);
        }
        return request;
    }
    
}
