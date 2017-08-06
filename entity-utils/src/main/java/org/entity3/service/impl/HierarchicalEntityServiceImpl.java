/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.service.impl;

import com.google.common.collect.Lists;
import org.entity3.IHierarchical;
import org.entity3.service.HierarchicalEntityService;
import org.springframework.data.jpa.domain.Specifications;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @author viktor
 */
public abstract class HierarchicalEntityServiceImpl<T extends IHierarchical<ID, T> , ID extends Serializable> extends EntityServiceImpl<T, ID> implements HierarchicalEntityService<T, ID>, org.entity3.service.AuditableEntityService<T, ID> {

    public HierarchicalEntityServiceImpl() {
    }

    public HierarchicalEntityServiceImpl(String... maskedProperty) {
        super(maskedProperty);
    }

    public HierarchicalEntityServiceImpl(Iterable<String> maskedProperties) {
        super(maskedProperties);
    }

    @Override
    public List<T> findByMaskAndEmptyChilds(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask,this.maskedPopertyList, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyChildsSpecification()));
    }

    @Override
    public List<T> findByMaskAndEmptyParents(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask,this.maskedPopertyList, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyParentsSpecification()));
    }

    @Override
    public List<T> findByEmptyChilds() {
        return findAll(HierarchicalServiceUtils.createEmptyChildsSpecification());
    }

    @Override
    public List<T> findByEmptyParents() {
        return findAll(HierarchicalServiceUtils.createEmptyParentsSpecification());
    }

}
