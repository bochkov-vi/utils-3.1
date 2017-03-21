/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;


import org.entity3.IChild;

/**
 * @param <T>
 * @author bochkov
 */
public class ParentIterator<T extends IChild<T,?>> extends RecursiveIterator<T> {

    public ParentIterator(boolean includeOriginal, T entity) {
        super(new ParentIteratorExtractor<T>(), includeOriginal, entity);
    }
}
