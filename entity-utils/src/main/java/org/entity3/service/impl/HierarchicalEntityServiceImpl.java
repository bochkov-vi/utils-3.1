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

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @author viktor
 */
public abstract class HierarchicalEntityServiceImpl<T extends IHierarchical<ID, T> & Auditable<?, ID>, ID extends Serializable> extends AuditableEntityServiceImpl<T, ID> implements HierarchicalEntityService<T, ID> {

    public HierarchicalEntityServiceImpl(Class<T> entityClass, String... maskeProperty) {
        super(entityClass, maskeProperty);
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


    public List<T> findByMaskAndEmtyChilds(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask, Lists.<Path>newArrayList())).and(createEmptyChildsSpecification()));
    }


    public List<T> findByMaskAndEmtyParents(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask, Lists.<Path>newArrayList())).and(createEmptyParentsSpecification()));
    }


    public List<T> findByEmptyChilds() {
        return findAll(createEmptyChildsSpecification());
    }


    public List<T> findByEmptyParents() {
        return findAll(createEmptyParentsSpecification());
    }

    protected Specification<T> createEmptyChildsSpecification() {
        return new Specification<T>() {

            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression e = root.get("childs");
                return cb.isEmpty(e);
            }
        };
    }

    protected Specification<T> createEmptyParentsSpecification() {
        return (root, query, cb) -> {
            Expression e = root.get("parents");
            return cb.isEmpty(e);
        };
    }


    public <S extends T> S save(S s) {
        return super.save(prepareSave(s));
    }

    public <S extends T> S prepareSave(S s) {
        //Устанавливаем всем деткам текущего предка если не установлен
        List<T> currentChilds = MoreObjects.firstNonNull(s.getChilds(), ImmutableList.<T>of());
        for (T child : currentChilds) {
            List<T> parentsOfChilds = MoreObjects.firstNonNull(child.getParents(), ImmutableList.<T>of());
            if (!parentsOfChilds.contains(s)) {
                child.setParents((ImmutableList.copyOf(ImmutableSet.copyOf(Iterables.concat(ImmutableList.of((T) s), parentsOfChilds)))));
                getRepository().save(child);
            }
        }
        //Устанавливаем всем предекам текущую детку
        List<T> currentParents = MoreObjects.firstNonNull(s.getParents(), ImmutableList.<T>of());
        for (T parent : currentParents) {
            List<T> childsOfParent = MoreObjects.firstNonNull(parent.getParents(), ImmutableList.<T>of());
            if (!childsOfParent.contains(s)) {
                parent.setChilds((ImmutableList.copyOf(ImmutableSet.copyOf(Iterables.concat(ImmutableList.of((T) s), childsOfParent)))));
                getRepository().save(parent);
            }
        }

        if (!s.isNew()) {
            ID id = s.getId();
            T old = getRepository().findOne(id);
            //Удалеяем ссылку на текущий объект у удаленных деток
            {List<T> oldChilds = MoreObjects.firstNonNull(old.getChilds(), ImmutableList.<T>of());
            for (T oldChildToRemove : Collections2.filter(oldChilds, Predicates.not(Predicates.in(currentChilds)))) {
                oldChildToRemove.setParents(ImmutableList.copyOf(Sets.difference(ImmutableSet.of((T) s), ImmutableSet.copyOf(MoreObjects.firstNonNull(oldChildToRemove.getParents(), ImmutableList.<T>of())))));
                getRepository().save(oldChildToRemove);
            }}
            //Удалеяем ссылку на текущий объект у удаленных предков
            List<T> oldParents = MoreObjects.firstNonNull(old.getParents(), ImmutableList.<T>of());
            for (T oldParentToRemove : Collections2.filter(oldParents, Predicates.not(Predicates.in(currentParents)))) {
                oldParentToRemove.setChilds(ImmutableList.copyOf(Sets.difference(ImmutableSet.of((T) s), ImmutableSet.copyOf(MoreObjects.firstNonNull(oldParentToRemove.getChilds(), ImmutableList.<T>of())))));
                getRepository().save(oldParentToRemove);
            }
        }
        return s;
    }

    @Override
    public <S extends T> S saveAndFlush(S s) {
        return super.saveAndFlush(prepareSave(s));
    }
}
