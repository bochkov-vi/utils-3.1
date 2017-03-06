/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.repository;

import org.entity3.IHierarchical;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @param <ID>
 * @author bochkov
 */
public interface HierarchicalRepository<T extends IHierarchical<ID, T>, ID extends Serializable> extends CustomRepository<T, ID> {
    @Query("select x from #{#entityName} x WHERE x.childs IS EMPTY ORDER BY x.name")
    List<T> findByChildListIsEmpty();

    @Query("select x from #{#entityName} x WHERE x.parents IS EMPTY ORDER BY x.name")
    List<T> findByParentListIsEmpty();

    @Query("select x from #{#entityName} x WHERE x.childs IS NOT EMPTY ORDER BY x.name")
    List<T> findByChildListIsNotEmpty();

    @Query("select x from #{#entityName} x WHERE x.parents IS NOT EMPTY ORDER BY x.name")
    List<T> findByParentListIsNotEmpty();
}
