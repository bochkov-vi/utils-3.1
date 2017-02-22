package org.entity3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by bochkov on 29.08.2014.
 */
public interface MultiSelectionRepository<T, ID> {
    <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec, Number limit);

    <P> List<P[]> findAll(MultiSelection<P> selection, Sort sort, Number limit);

    <P> List<P[]> findAll(MultiSelection<P> selection, Iterable<ID> ids, Number limit);

    <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec, Sort sort, Number limit);

    <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec);

    <P> List<P[]> findAll(MultiSelection<P> selection, Sort sort);

    <P> List<P[]> findAll(MultiSelection<P> selection, Iterable<ID> ids);

    <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec, Sort sort);

}
