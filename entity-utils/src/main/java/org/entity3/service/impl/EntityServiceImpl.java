/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.service.impl;

import org.entity3.repository.PropertySelection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @param <ID>
 * @author viktor
 */

public abstract class EntityServiceImpl<T, ID extends Serializable> extends UnTransactioanalEntityServiceImpl<T, ID> {
    public EntityServiceImpl() {
        super();
    }

    public EntityServiceImpl(Class<T> entityClass) {
        super(entityClass);
    }

    public EntityServiceImpl(String... maskedProperty) {
        super(maskedProperty);
    }

    public EntityServiceImpl(Class<T> entityClass, String... maskedProperty) {
        super(entityClass, maskedProperty);
    }

    public EntityServiceImpl(Class<T> entityClass, List<String> maskedPopertyList) {
        super(entityClass, maskedPopertyList);
    }

    @Transactional
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.save(entities);
    }


    public void flush() {
        super.flush();
    }

    @Transactional

    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    @Transactional

    public void deleteInBatch(Iterable<T> entities) {
        super.deleteInBatch(entities);
    }

    @Transactional

    public void deleteAllInBatch() {
        super.deleteAllInBatch();
    }

    @Transactional

    public <S extends T> S save(S s) {
        return super.save(s);
    }

    @Transactional

    public void delete(ID id) {
        super.delete(id);
    }

    @Transactional

    public void delete(T t) {
        super.delete(t);
    }

    @Transactional

    public void delete(Iterable<? extends T> itrbl) {
        super.delete(itrbl);
    }

    @Transactional

    public void deleteAll() {
        super.deleteAll();
    }


    public List<T> findAll(Specification<T> spec, Sort sort) {
        return super.findAll(spec, sort);
    }


    public long count(Specification<T> spec) {
        return super.count(spec);
    }


    public T getOne(ID id) {
        return super.getOne(id);
    }


    public List<T> findAll(Number limit) {
        return super.findAll(limit);
    }


    public List<T> findAll(Specification<T> spec, Number limit) {
        return super.findAll(spec, limit);
    }


    public List<T> findAll(Sort sort, Number limit) {
        return super.findAll(sort, limit);
    }


    public List<T> findAll(Iterable<ID> ids, Number limit) {
        return super.findAll(ids, limit);
    }


    public List<T> findAll(Specification<T> spec, Sort sort, Number limit) {
        return super.findAll(spec, sort, limit);
    }


    public List<ID> findAllId(Number limit) {
        return super.findAllId(limit);
    }


    public List<ID> findAllId(Specification<T> spec, Number limit) {
        return super.findAllId(spec, limit);
    }


    public List<ID> findAllId(Sort sort, Number limit) {
        return super.findAllId(sort, limit);
    }


    public List<ID> findAllId(Iterable<ID> ids, Number limit) {
        return super.findAllId(ids, limit);
    }


    public List<ID> findAllId(Specification<T> spec, Sort sort, Number limit) {
        return super.findAllId(spec, sort, limit);
    }


    public T findOneByLimit(Specification<T> spec) {
        return super.findOneByLimit(spec);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Number limit) {
        return super.findAll(selection, spec, limit);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Sort sort, Number limit) {
        return super.findAll(selection, sort, limit);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Iterable<ID> ids, Number limit) {
        return super.findAll(selection, ids, limit);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Sort sort, Number limit) {
        return super.findAll(selection, spec, sort, limit);
    }


    public boolean exists(ID id) {
        return super.exists(id);
    }


    public T findOne(Specification<T> spec) {
        return super.findOne(spec);
    }


    public List<T> findAll(Specification<T> spec) {
        return super.findAll(spec);
    }


    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return super.findAll(spec, pageable);
    }


    public Page<T> findAll(Pageable pgbl) {
        return super.findAll(pgbl);
    }


    public T findOne(ID id) {
        return super.findOne(id);
    }


    public long count() {
        return super.count();
    }


    public List<T> findAll() {
        return super.findAll();
    }


    public List<T> findAll(Sort sort) {
        return super.findAll(sort);
    }


    public List<T> findAll(Iterable<ID> ids) {
        return super.findAll(ids);
    }


    public List<T> findByMask(String mask) {
        return super.findByMask(mask);
    }


    public List<ID> findIdByMask(String mask) {
        return super.findIdByMask(mask);
    }


    public <P> List<P> findPropertyByMask(String propertyPath, String mask) {
        return super.findPropertyByMask(propertyPath, mask);
    }


    public <P> List<P> findPropertyByMask(String propertyPath, String mask, String... maskedProperties) {
        return super.findPropertyByMask(propertyPath, mask, maskedProperties);
    }

    @Override
    public T refresh(T entity) {
        return getRepository().refresh(entity);
    }
}