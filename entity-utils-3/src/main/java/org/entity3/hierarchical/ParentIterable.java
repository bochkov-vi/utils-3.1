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
public class ParentIterable<T extends IHierarchical> extends RecursiveIterable<T> {

    public ParentIterable(Iterable<T> e) {
        super(e);
    }

    public ParentIterable(boolean includeOriginal, T... e) {
        super(includeOriginal, e);
    }

    public ParentIterable(T... e) {
        super(e);
    }

    public ParentIterable(boolean includeOriginal, Iterable<T> e) {
        super(includeOriginal, e);
    }


    public Iterator<T> iterator() {
        return new ParentIterator<T>(includeOriginal, this.e);
    }

}
