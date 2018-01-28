/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.service;

import org.entity3.IHierarchical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * @author viktor
 */
public interface HierarchicalEntityService<T extends IHierarchical<ID, T> , ID extends Serializable> extends AuditableEntityService<T, ID> {

    List<T> findByEmptyParents();

    List<T> findByEmptyChilds();

    List<T> findByMaskAndEmptyChilds(String mask);

    List<T> findByMaskAndEmptyParents(String mask);

    List<T> findByMaskAndEmptyChilds(String mask, Integer limit);

    List<T> findByMaskAndEmptyParents(String mask, Integer limit);

    Page<T> findByMaskAndEmptyChilds(String mask, Pageable pageable);

    Page<T> findByMaskAndEmptyParents(String mask, Pageable pageable);

    List<T> findByEmptyChilds(Integer limit);

    List<T> findByEmptyParents(Integer limit);
}
