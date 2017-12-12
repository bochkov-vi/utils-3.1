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
        return findByMaskAndEmptyChilds(mask,MAX_MASKED_RESULT);
    }

    @Override
    public List<T> findByMaskAndEmptyParents(String mask) {
        return findByMaskAndEmptyParents(mask,MAX_MASKED_RESULT);
    }

    @Override
    public List<T> findByEmptyChilds() {
        return findByEmptyChilds(MAX_MASKED_RESULT);
    }

    @Override
    public List<T> findByEmptyParents() {
        return findByEmptyParents(MAX_MASKED_RESULT);
    }

    @Override
    public List<T> findByMaskAndEmptyChilds(String mask, Integer limit) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask,this.maskedPopertyArray, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyChildsSpecification()),limit);
    }

    @Override
    public List<T> findByMaskAndEmptyParents(String mask, Integer limit) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask,this.maskedPopertyArray, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyParentsSpecification()),limit);
    }

    @Override
    public List<T> findByEmptyChilds(Integer limit) {
        return findAll(HierarchicalServiceUtils.createEmptyChildsSpecification(),limit);
    }

    @Override
    public List<T> findByEmptyParents(Integer limit) {
        return findAll(HierarchicalServiceUtils.createEmptyParentsSpecification(),limit);
    }

}
