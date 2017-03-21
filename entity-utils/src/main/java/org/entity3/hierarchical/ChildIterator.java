/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import org.entity3.IParent;

/**
 * @param <T>
 * @author bochkov
 */
public class ChildIterator<T extends IParent> extends RecursiveIterator<T> {

    public ChildIterator(boolean includeOriginal, T entity) {
        super(new ChildIteratorExtractor<T>(), includeOriginal, entity);
    }
}
