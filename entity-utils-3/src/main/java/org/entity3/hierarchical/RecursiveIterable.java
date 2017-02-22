/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import org.entity3.IHierarchical;
import com.google.common.collect.Lists;

/**
 * @param <T>
 * @author bochkov
 */
public abstract class RecursiveIterable<T extends IHierarchical> implements Iterable<T> {

    Iterable<T> e;
    boolean includeOriginal;

    public RecursiveIterable(Iterable<T> e) {
        this.e = e;
        includeOriginal = false;
    }

    public RecursiveIterable(boolean includeOriginal, Iterable<T> e) {
        this.e = e;
        this.includeOriginal = includeOriginal;
    }

    public RecursiveIterable(T... e) {
        this.e = Lists.newArrayList(e);
        this.includeOriginal = false;
    }

    public RecursiveIterable(boolean includeOriginal, T... e) {
        this.e = Lists.newArrayList(e);
        this.includeOriginal = includeOriginal;
    }
}
