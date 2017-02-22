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
public class ChildIterable<T extends IHierarchical> extends RecursiveIterable<T> {

    public ChildIterable(Iterable<T> e) {
        super(e);
    }

    public ChildIterable(T... e) {
        super(e);
    }

    public ChildIterable(boolean includeOriginal, T... e) {
        super(includeOriginal, e);
    }

    public ChildIterable(boolean includeOriginal, Iterable<T> e) {
        super(includeOriginal, e);
    }

    public Iterator<T> iterator() {
        return new ChildIterator<T>(includeOriginal, this.e);
    }

}
