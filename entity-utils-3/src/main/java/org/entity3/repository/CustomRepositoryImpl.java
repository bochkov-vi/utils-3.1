/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.repository;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * @author bochkov
 */
@NoRepositoryBean
public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {

    protected final JpaEntityInformation<T, ?> entityInformation;
    protected final PropertySelection<ID> idSelection;
    protected final EntityManager em;


    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
        this.entityInformation = entityInformation;
        this.idSelection = (root, query, cb) -> root.get(entityInformation.getIdAttribute());
    }


    public List<T> findAll(final Iterable<ID> ids) {
        return findAll(createIdsSpecification(ids));
    }


    public List<T> findAll(Number limit) {
        return findAll((Specification<T>) null, null, limit);
    }


    public List<T> findAll(Specification<T> spec, Number limit) {
        return findAll(spec, null, limit);
    }


    public List<T> findAll(Sort sort, Number limit) {
        return findAll((Specification<T>) null, sort, limit);
    }


    public List<T> findAll(Iterable<ID> ids, Number limit) {
        return findAll(createIdsSpecification(ids), null, limit);
    }


    public List<T> findAll(Specification<T> spec, Sort sort, Number limit) {
        TypedQuery<T> query = getQuery(spec, sort);
        if (limit != null) {
            query.setMaxResults(limit.intValue());
        }
        return query.getResultList();
    }


    public List<ID> findAllId(Number limit) {
        return findAll(idSelection, (Sort) null, limit);
    }


    public T findOne(Specification<T> spec) {
        return Iterables.getFirst(findAll(spec, 1), null);
    }


    public List<ID> findAllId(Specification<T> spec, Number limit) {
        return findAll(idSelection, spec, limit);
    }


    public List<ID> findAllId(Sort sort, Number limit) {
        return findAll(idSelection, sort, limit);
    }


    public List<ID> findAllId(Iterable<ID> ids, Number limit) {
        return findAll(idSelection, ids, limit);
    }


    public List<ID> findAllId(Specification<T> spec, Sort sort, Number limit) {
        return findAll(idSelection, sort, limit);
    }


    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Number limit) {
        return findAll(selection, spec, null, limit);
    }

    public <P> List<P> findAll(PropertySelection<P> selection, Sort sort, Number limit) {
        return findAll(selection, null, sort, limit);
    }

    public <P> List<P> findAll(PropertySelection<P> selection, Iterable<ID> ids, Number limit) {
        return findAll(selection, createIdsSpecification(ids), null, null);
    }

    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Sort sort, Number limit) {
        TypedQuery<P> query = getPropertyQuery(selection, spec, sort);
        if (limit != null) {
            query.setMaxResults(limit.intValue());
        }
        return query.getResultList();
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec) {
        return findAll(selection, spec, null, null);
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Sort sort) {
        return findAll(selection, null, sort, null);
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Iterable<ID> ids) {
        return findAll(selection, createIdsSpecification(ids), null, null);
    }

    @Override
    public <P> List<P> findAll(PropertySelection<P> selection, Specification<T> spec, Sort sort) {
        return null;
    }

    @Override
    public <P> P findOne(PropertySelection<P> selection, Specification<T> spec, Sort sort) {
        return Iterables.getFirst(findAll(selection, spec, sort, 1), null);
    }

    @Override
    public T findOne(Specification<T> spec, Sort sort) {
        return Iterables.getFirst(findAll(spec, sort, 1), null);
    }


    public long count(PropertySelection<Long> selection, Specification<T> spec) {
        return count(selection, spec, true);
    }

    public long count(PropertySelection<Long> selection, Specification<T> spec, boolean distinct) {
        return getCountPropertyQuery(selection, spec, distinct).getSingleResult();
    }

    protected Specification<T> createIdsSpecification(final Iterable<ID> ids) {
        if (ids != null && ids.iterator().hasNext()) {
            return (Specification<T>) (root, query, cb) -> {
                Path<?> path = root.get(entityInformation.getIdAttribute());
                return path.in((Collection) ids);
            };
        }
        return null;
    }


    protected <P> TypedQuery<P> getCountPropertyQuery(PropertySelection<P> sel, Specification<T> spec, boolean distinct) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<T> root = applySpecificationToCriteria(spec, query);
        if (distinct) {
            query.select(builder.countDistinct((Expression) sel.toSelection(root, query, builder)));
        } else {
            query.select(builder.count((Expression) sel.toSelection(root, query, builder)));
        }
        return em.createQuery(query);
    }


    protected <P> TypedQuery<P> getPropertyQuery(PropertySelection<P> sel, Specification<T> spec, Sort sort) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<T> root = applySpecificationToCriteria(spec, query);
        query.select(sel.toSelection(root, query, builder)).distinct(true);
        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }
        return em.createQuery(query);
    }


    protected <P> TypedQuery<P> getMultiPropertyQuery(MultiSelection<P> sel, Specification<T> spec, Sort sort, boolean distinct) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<T> root = applySpecificationToCriteria(spec, query);
        query.multiselect(Lists.newArrayList(sel.toSelection(root, query, builder))).distinct(distinct);
        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }
        return em.createQuery(query);
    }

    private <S> Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<S> query) {
        Assert.notNull(query);
        Root<T> root = query.from(getDomainClass());
        if (spec == null) {
            return root;
        }
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);
        if (predicate != null) {
            query.where(predicate);
        }
        return root;
    }

    @Override
    public List findAll(MultiSelection selection, Specification spec, Number limit) {
        return findAll(selection, spec, null, limit);
    }

    @Override
    public List findAll(MultiSelection selection, Sort sort, Number limit) {
        return findAll(selection, null, sort, limit);
    }

    @Override
    public List findAll(MultiSelection selection, Iterable iterable, Number limit) {
        return findAll(selection, createIdsSpecification(iterable), null, limit);
    }

    @Override
    public List findAll(MultiSelection selection, Specification spec, Sort sort, Number limit) {
        TypedQuery<T> query = getMultiPropertyQuery(selection, spec, sort, true);
        if (limit != null) {
            query.setMaxResults(limit.intValue());
        }
        return query.getResultList();
    }


    @Override
    public List findAll(MultiSelection selection, Specification spec) {
        return findAll(selection, spec, null, null);
    }

    @Override
    public List findAll(MultiSelection selection, Sort sort) {
        return findAll(selection, null, sort, null);
    }

    @Override
    public List findAll(MultiSelection selection, Iterable iterable) {
        return findAll(selection, createIdsSpecification(iterable), null, null);
    }

    @Override
    public List findAll(MultiSelection selection, Specification spec, Sort sort) {
        return findAll(selection, spec, sort, null);
    }
}
