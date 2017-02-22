/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.service;

import org.entity3.IHierarchical;
import org.springframework.data.domain.Auditable;

import java.io.Serializable;
import java.util.List;

/**
 * @author viktor
 */
public interface HierarchicalEntityService<T extends IHierarchical<ID,T> & Auditable<?, ID>, ID extends Serializable> extends AuditableEntityService<T, ID> {
    public List<T> findByEmptyParents();

    public List<T> findByEmptyChilds();

    public List<T> findByMaskAndEmtyChilds(String mask);

    public List<T> findByMaskAndEmtyParents(String mask);
}
