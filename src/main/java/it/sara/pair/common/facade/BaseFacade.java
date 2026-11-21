package it.sara.pair.common.facade;

public interface BaseFacade<C, ID> {
    C create(C request);
    C detail(ID id);
    C update(ID id, C request);
    C delete(ID id);
    
}
