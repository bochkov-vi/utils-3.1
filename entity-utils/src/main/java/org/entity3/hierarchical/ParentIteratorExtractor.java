/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import org.entity3.IHierarchical;

import java.util.Iterator;

/**
 * @param <T>
 * @author bochkov
 */
public class ParentIteratorExtractor<T extends IHierarchical> implements IteratorExtractor<T> {


    public Iterator<T> extractIterator(T e) {
        if (e != null && e.getParents() != null && !e.getParents().isEmpty() && e.isCanHaveParents()) {
            return e.getParents().iterator();
        } else {
            return null;
        }
    }
}
