package org.entity3.repository;

/**
 * Created by home on 10.03.17.
 */
public interface RefreshableRepository<T> {
    T refresh(T entity);
}
