package org.entity3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by bochkov on 29.08.2014.
 */
public interface PropertySelectionRepository<T, ID> {
    <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Number limit);

    <P> List<P> findAll(PropertySelection<P> selection, Sort sort, Number limit);

    <P> List<P> findAll(PropertySelection<P> selection, Iterable<ID> ids, Number limit);

    <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Sort sort, Number limit);

    <P> Page<P> findAll(PropertySelection<P> selection, Specification<T> spec, Pageable pageable);

    <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec);

    <P> List<P> findAll(PropertySelection<P> selection, Sort sort);


    <P> List<P> findAll(PropertySelection<P> selection, Iterable<ID> ids);

    <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Sort sort);

    <P> P findOne(PropertySelection<P> selection, Specification<T> spec, Sort sort);


    <P> long count(PropertySelection<P> selection, Specification<T> spec);

    <P> long count(PropertySelection<P> selection, Specification<T> spec, boolean distinct);
}
