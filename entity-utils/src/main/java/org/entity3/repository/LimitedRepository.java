package org.entity3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bochkov on 31.03.14.
 */
public interface LimitedRepository<T, ID extends Serializable> {

    List<T> findAll(Number limit);

    List<T> findAll(Specification<T> spec, Number limit);

    List<T> findAll(Sort sort, Number limit);

    List<T> findAll(Iterable<ID> ids, Number limit);

    List<T> findAll(Specification<T> spec, Sort sort, Number limit);

    List<ID> findAllId(Number limit);

    List<ID> findAllId(Specification<T> spec, Number limit);

    List<ID> findAllId(Sort sort, Number limit);

    List<ID> findAllId(Iterable<ID> ids, Number limit);

    List<ID> findAllId(Specification<T> spec, Sort sort, Number limit);

    T findOne(Specification<T> spec, Sort sort);
}