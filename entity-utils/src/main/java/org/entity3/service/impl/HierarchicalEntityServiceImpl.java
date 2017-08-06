/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.service.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import org.entity3.IHierarchical;
import org.entity3.service.HierarchicalEntityService;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @param <T>
 * @author viktor
 */
public abstract class HierarchicalEntityServiceImpl<T extends IHierarchical<ID, T> & Auditable<?, ID>, ID extends Serializable> extends AuditableEntityServiceImpl<T, ID> implements HierarchicalEntityService<T, ID> {

    public HierarchicalEntityServiceImpl(Class<T> entityClass, String... maskedProperty) {
        super(entityClass, maskedProperty);
    }

    public HierarchicalEntityServiceImpl(Class<T> entityClass, List<String> maskedPopertyList) {
        super(entityClass, maskedPopertyList);
    }

    protected HierarchicalEntityServiceImpl() {
        super();
    }

    public HierarchicalEntityServiceImpl(Class<T> entityClass) {
        super(entityClass);
    }

    public HierarchicalEntityServiceImpl(String... maskedProperty) {
        super(maskedProperty);
    }

    @Override
    public List<T> findByMaskAndEmptyChilds(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask, Lists.newArrayList())).and(createEmptyChildsSpecification()));
    }

    @Override
    public List<T> findByMaskAndEmptyParents(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask, Lists.newArrayList())).and(createEmptyParentsSpecification()));
    }

    @Override
    public List<T> findByEmptyChilds() {
        return findAll(createEmptyChildsSpecification());
    }

    @Override
    public List<T> findByEmptyParents() {
        return findAll(createEmptyParentsSpecification());
    }

    protected Specification<T> createEmptyChildsSpecification() {
        return (root, query, cb) -> {
            Expression e = root.get("childs");
            return cb.isEmpty(e);
        };
    }

    protected Specification<T> createEmptyParentsSpecification() {
        return (root, query, cb) -> {
            Expression e = root.get("parents");
            return cb.isEmpty(e);
        };
    }

    @Transactional
    public <S extends T> S save(S s) {
        return super.save(prepareSave(s));
    }

    public <S extends T> S prepareSave(S s) {
        List<T> currentChilds = MoreObjects.firstNonNull(s.getChilds(), ImmutableList.<T>of());
        List<T> currentParents = MoreObjects.firstNonNull(s.getParents(), ImmutableList.<T>of());

        if (!s.isNew()) {
            Collection<T> childsRemoveFrom = ImmutableList.of();
            Collection<T> parentsRemoveFrom = ImmutableList.of();
            T old = getRepository().findOne(s.getId());
            childsRemoveFrom = Collections2.filter(old.getChilds(), Predicates.not(Predicates.in(currentChilds)));
            parentsRemoveFrom = Collections2.filter(old.getParents(), Predicates.not(Predicates.in(currentParents)));
            for (T e : childsRemoveFrom) {
                e.getParents().remove(e);
                getRepository().saveAndFlush(e);
            }
            for (T e : parentsRemoveFrom) {
                e.getChilds().remove(e);
                getRepository().saveAndFlush(e);
            }
        } else {
            s = getRepository().save(s);
        }

        //Устанавливаем всем деткам текущего предка если не установлен
        for (T child : currentChilds) {
            List<T> parentsOfChilds = MoreObjects.firstNonNull(child.getParents(), ImmutableList.<T>of());
            if (!parentsOfChilds.contains(s)) {
                child.setParents((ImmutableList.copyOf(ImmutableSet.copyOf(Iterables.concat(ImmutableList.of((T) s), parentsOfChilds)))));
                getRepository().save(child);
            }
        }

        //Устанавливаем всем предекам текущую детку
        for (T parent : currentParents) {
            List<T> childsOfParent = MoreObjects.firstNonNull(parent.getChilds(), ImmutableList.<T>of());
            if (!childsOfParent.contains(s)) {
                parent.setChilds((ImmutableList.copyOf(ImmutableSet.copyOf(Iterables.concat(ImmutableList.of((T) s), childsOfParent)))));
                getRepository().save(parent);
            }
        }

        s.setParents(currentParents);
        s.setChilds(currentChilds);
        return s;
    }

    @Transactional
    @Override
    public <S extends T> S saveAndFlush(S s) {
        return super.saveAndFlush(prepareSave(s));
    }


}
