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

    protected HierarchicalEntityServiceImpl() {
        super();
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
                Expression e = root.get("childList");
                return cb.isEmpty(e);
            }
        };
    }

    protected Specification<T> createEmptyParentsSpecification() {
        return new Specification<T>() {

            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression e = root.get("parentList");
                return cb.isEmpty(e);
            }
        };
    }


    public <S extends T> S save(S s) {
        List<T> currentChilds = MoreObjects.firstNonNull(s.getChilds(), ImmutableList.<T>of());

        for (T child : currentChilds) {
            List<T> parents = MoreObjects.firstNonNull(child.getParents(), ImmutableList.<T>of());
            if (!parents.contains(s)) {
                child.setParents((ImmutableList.copyOf(ImmutableSet.copyOf(Iterables.concat(ImmutableList.of((T) s), parents)))));
            }
        }
        if (!s.isNew()) {
            ID id = s.getId();
            T old = getRepository().findOne(id);
            List<T> oldChilds = MoreObjects.firstNonNull(old.getChilds(), ImmutableList.<T>of());
            for (T ch : Collections2.filter(oldChilds, Predicates.not(Predicates.in(currentChilds)))) {
                ch.setParents(ImmutableList.copyOf(Sets.difference(ImmutableSet.of((T) s), ImmutableSet.copyOf(MoreObjects.firstNonNull(ch.getParents(), ImmutableList.<T>of())))));
                getRepository().save(ch);
            }
        }
        return super.save(s);
    }


}
