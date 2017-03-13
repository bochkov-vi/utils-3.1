/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.service.impl;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.entity3.repository.CustomRepository;
import org.entity3.repository.MultiSelection;
import org.entity3.repository.PropertySelection;
import org.entity3.service.EntityService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;


/**
 * @param <T>
 * @param <ID>
 * @author viktor
 */

public abstract class UnTransactioanalEntityServiceImpl<T, ID extends Serializable> implements EntityService<T, ID> {
    public static int MAX_MASKED_RESULT = 10;

    protected Class<T> entityClass;

    protected List<String> maskedPopertyList;


    public UnTransactioanalEntityServiceImpl() {
        entityClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public UnTransactioanalEntityServiceImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public UnTransactioanalEntityServiceImpl(String... maskedProperty) {
        this();
        this.maskedPopertyList = ImmutableList.copyOf(maskedProperty);
    }

    public UnTransactioanalEntityServiceImpl(Class<T> entityClass, String... maskedProperty) {
        this.entityClass = entityClass;
        maskedPopertyList = ImmutableList.copyOf(maskedProperty);
    }

    public UnTransactioanalEntityServiceImpl(Class<T> entityClass, List<String> maskedPopertyList) {
        this.entityClass = entityClass;
        this.maskedPopertyList = ImmutableList.copyOf(maskedPopertyList);
    }

    protected static Path createPath(From root, String path, Collection<Path> pathCache) {
        Path result = root;
        for (String attributeName : path.split("\\.")) {
            if (attributeName.length() > 0) {
                if (Persistable.class.isAssignableFrom(result.get(attributeName).getJavaType())) {
                    result = root.join(attributeName, JoinType.LEFT);
                } else {
                    result = result.get(attributeName);
                }
                if (pathCache != null) {
                    boolean addToCache = true;
                    for (Path p : pathCache) {
                        if (p.getModel().equals(result.getModel())) {
                            result = p;
                            addToCache = false;
                            break;
                        }
                    }
                    if (addToCache) {
                        pathCache.add(result);
                    }
                }
            }
        }
        return result;
    }

    protected abstract CustomRepository<T, ID> getRepository();

    private Predicate createStringMaskExpression(String mask, Expression maskedProperty, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.like(maskedProperty.as(String.class), "%" + mask.trim() + "%");
    }

    protected Predicate createMaskExpression(String mask, Expression maskedProperty, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return createStringMaskExpression(mask, maskedProperty, query, cb);
    }

    protected Specification<T> createFindByMaskSpecification(final String mask, final String maskedPopertyName, final Collection<Path> pathCache) {
        return (root, query, cb) -> {
            Predicate result;
            Path maskedProperty = createPath(root, maskedPopertyName, pathCache);
            result = createMaskExpression(mask, maskedProperty, query, cb);
            if (result != null) {
                List<Order> orders;
                if (query.getOrderList() == null) {
                    orders = ImmutableList.of();
                } else {
                    orders = ImmutableList.copyOf(query.getOrderList());
                }
                orders = ImmutableList.<Order>builder().add(cb.asc(cb.locate(maskedProperty, mask)), cb.asc(maskedProperty.isNull()), cb.asc(cb.length(maskedProperty)), cb.asc(maskedProperty)).addAll(orders).build();
                query.orderBy(orders);
            }
            return result;
        };
    }


    public List<T> findByMask(String mask) {
        Specification where = createFindByMaskSpecification(mask, Lists.<Path>newArrayList());
        if (where != null) {
            return getRepository().findAll(where, MAX_MASKED_RESULT);
        }
        return getRepository().findAll(MAX_MASKED_RESULT);
    }


    public List<ID> findIdByMask(String mask) {
        Specification where = createFindByMaskSpecification(mask, Lists.<Path>newArrayList());
        return getRepository().findAllId(MAX_MASKED_RESULT);
    }


    public <P> List<P> findPropertyByMask(String propertyPath, String mask) {
        Collection<Path> pathCashe = Lists.<Path>newArrayList();
        Specification where = createFindByMaskSpecification(mask, propertyPath, pathCashe);
        return getRepository().<P>findAll(this.<P>createPropertySelection(propertyPath, pathCashe), where, MAX_MASKED_RESULT);
    }


    public <P> List<P> findPropertyByMask(String propertyPath, String mask, Specification additionSpecification) {
        Collection<Path> pathCashe = Lists.<Path>newArrayList();
        Specifications where = Specifications.where(createFindByMaskSpecification(mask, propertyPath, pathCashe));
        if (additionSpecification != null) {
            where = where.and(additionSpecification);
        }
        return getRepository().<P>findAll(this.<P>createPropertySelection(propertyPath, pathCashe), where, MAX_MASKED_RESULT);
    }


    public <P> List<P> findPropertyByMask(String propertyPath, String mask, String... maskedProperties) {
        List<Path> pathCache = Lists.newArrayList();
        Specifications where = Specifications.where(createFindByMaskSpecification(mask, Lists.newArrayList(maskedProperties), pathCache));
        return getRepository().<P>findAll(this.<P>createPropertySelection(propertyPath, pathCache), where, MAX_MASKED_RESULT);
    }


    public <P> List<P> findPropertyByMask(String propertyPath, String mask, Specification additionSpecification, String... maskedProperties) {
        List<Path> pathCache = Lists.newArrayList();
        Specifications where = Specifications.where(createFindByMaskSpecification(mask, Lists.newArrayList(maskedProperties), pathCache));
        if (additionSpecification != null) {
            where = where.and(additionSpecification);
        }
        return getRepository().<P>findAll(this.<P>createPropertySelection(propertyPath, pathCache), where, MAX_MASKED_RESULT);
    }


    protected Specification<T> createFindByMaskSpecification(final String mask, Collection<Path> pathCashe) {
        return createFindByMaskSpecification(mask, this.maskedPopertyList, pathCashe);
    }

    protected <P> PropertySelection<P> createPropertySelection(final String propertyPath, final Collection<Path> pathCashe) {
        return createPropertySelection(propertyPath, true, pathCashe);
    }

    protected <P> PropertySelection<P> createPropertySelection(final String propertyPath, boolean distinct, final Collection<Path> pathCashe) {
        return (root, query, cb) -> {
            query.distinct(distinct);
            return createPath(root, propertyPath, pathCashe);
        };
    }

    protected Specification<T> createFindByMaskSpecification(final String mask, Iterable<String> maskedPoperties, Collection<Path> pathCashe) {
        Specifications<T> where = null;
        for (String propertyName : MoreObjects.firstNonNull(maskedPoperties, ImmutableList.<String>of())) {
            Specification<T> spec = createFindByMaskSpecification(mask, propertyName, pathCashe);
            if (where == null) {
                where = Specifications.where(spec);
            } else {
                where = where.or(spec);
            }
        }
        return where;
    }

    @Override
    public T findOne(Specification<T> spec, Sort sort) {
        return getRepository().findOne(spec, sort);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }


    public List<T> findAll(Sort sort) {
        return getRepository().findAll(sort);
    }


    public List<T> findAll(Iterable<ID> ids) {
        return getRepository().findAll(ids);
    }


    public <S extends T> List<S> save(Iterable<S> entities) {
        return getRepository().save(entities);
    }


    public void flush() {
        getRepository().flush();
    }


    public <S extends T> S saveAndFlush(S entity) {
        return getRepository().saveAndFlush(entity);
    }


    public void deleteInBatch(Iterable<T> entities) {
        getRepository().deleteInBatch(entities);
    }


    public void deleteAllInBatch() {
        getRepository().deleteAllInBatch();
    }


    public Page<T> findAll(Pageable pgbl) {
        return getRepository().findAll(pgbl);
    }


    public <S extends T> S save(S s) {
        return getRepository().save(s);
    }


    public T findOne(ID id) {
        return getRepository().findOne(id);
    }


    public boolean exists(ID id) {
        return getRepository().exists(id);
    }


    public long count() {
        return getRepository().count();
    }


    public void delete(ID id) {
        getRepository().delete(id);
    }


    public void delete(T t) {
        getRepository().delete(t);
    }


    public void delete(Iterable<? extends T> itrbl) {
        getRepository().delete(itrbl);
    }


    public void deleteAll() {
        getRepository().deleteAll();
    }


    public T findOne(Specification<T> spec) {
        return getRepository().findOne(spec);
    }


    public List<T> findAll(Specification<T> spec) {
        return getRepository().findAll(spec);
    }


    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return getRepository().findAll(spec, pageable);
    }


    public List<T> findAll(Specification<T> spec, Sort sort) {
        return getRepository().findAll(spec, sort);
    }


    public long count(Specification<T> spec) {
        return getRepository().count(spec);
    }


    public T getOne(ID id) {
        return getRepository().findOne(id);
    }


    public List<T> findAll(Number limit) {
        return getRepository().findAll(limit);
    }


    public List<T> findAll(Specification<T> spec, Number limit) {
        return getRepository().findAll(spec, limit);
    }


    public List<T> findAll(Sort sort, Number limit) {
        return getRepository().findAll(sort, limit);
    }


    public List<T> findAll(Iterable<ID> ids, Number limit) {
        return getRepository().findAll(ids, limit);
    }


    public List<T> findAll(Specification<T> spec, Sort sort, Number limit) {
        return getRepository().findAll(spec, sort, limit);
    }


    public List<ID> findAllId(Number limit) {
        return getRepository().findAllId(limit);
    }


    public List<ID> findAllId(Specification<T> spec, Number limit) {
        return getRepository().findAllId(spec, limit);
    }


    public List<ID> findAllId(Sort sort, Number limit) {
        return getRepository().findAllId(sort, limit);
    }


    public List<ID> findAllId(Iterable<ID> ids, Number limit) {
        return getRepository().findAllId(ids, limit);
    }


    public List<ID> findAllId(Specification<T> spec, Sort sort, Number limit) {
        return getRepository().findAllId(spec, sort, limit);
    }


    public T findOneByLimit(Specification<T> spec) {
        return getRepository().findOne(spec);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Number limit) {
        return getRepository().findAll(selection, spec, limit);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Sort sort, Number limit) {
        return getRepository().findAll(selection, sort, limit);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Iterable<ID> ids, Number limit) {
        return getRepository().findAll(selection, ids, limit);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Sort sort, Number limit) {
        return getRepository().findAll(selection, spec, sort, limit);
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec) {
        return getRepository().findAll(selection, spec);
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Sort sort) {
        return getRepository().findAll(selection, sort);
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Iterable<ID> ids) {
        return getRepository().findAll(selection, ids);
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Sort sort) {
        return getRepository().findAll(selection, spec, sort);
    }


    public long count(PropertySelection<Long> selection, Specification<T> spec) {
        return getRepository().count(selection, spec);
    }


    public long count(PropertySelection<Long> selection, Specification<T> spec, boolean distinct) {
        return getRepository().count(selection, spec, distinct);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return getRepository().findAll(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return getRepository().findAll(example, sort);
    }

    @Override
    public <S extends T> S findOne(Example<S> example) {
        return getRepository().findOne(example);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return getRepository().findAll(example, pageable);
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return getRepository().count(example);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return getRepository().exists(example);
    }


    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec, Number limit) {
        return getRepository().findAll(selection, spec, limit);
    }

    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Sort sort, Number limit) {
        return getRepository().findAll(selection, sort, limit);
    }

    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Iterable<ID> ids, Number limit) {
        return getRepository().findAll(selection, ids, limit);
    }

    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec, Sort sort, Number limit) {
        return getRepository().findAll(selection, spec, sort, limit);
    }

    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec) {
        return getRepository().findAll(selection, spec);
    }

    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Sort sort) {
        return getRepository().findAll(selection, sort);
    }

    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Iterable<ID> ids) {
        return getRepository().findAll(selection, ids);
    }

    @Override
    public <P> List<P[]> findAll(MultiSelection<P> selection, Specification<T> spec, Sort sort) {
        return getRepository().findAll(selection, spec, sort);
    }

    @Override
    public <P> P findOne(PropertySelection<P> selection, Specification<T> spec, Sort sort) {
        return getRepository().findOne(selection, spec, sort);
    }
}
