package it.sara.pair.common.service;

public interface BaseService<T, ID> {
    T detail(ID id);
    T create(T entity);
    T delete(ID id);
    T update(T entity);
}
