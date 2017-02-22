/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import org.entity3.IHierarchical;

/**
 * @param <T>
 * @author bochkov
 */
public class ParentIterator<T extends IHierarchical> extends RecursiveIterator<T> {

    public ParentIterator(boolean includeOriginal, Iterable<T> iterable) {
        super(new ParentIteratorExtractor<T>(), includeOriginal, iterable);
    }

    public ParentIterator(boolean includeOriginal, T... iterable) {
        super(new ParentIteratorExtractor<T>(), includeOriginal, iterable);
    }

}
