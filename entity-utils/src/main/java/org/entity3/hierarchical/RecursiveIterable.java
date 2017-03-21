/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

/**
 * @param <T>
 * @author bochkov
 */
public abstract class RecursiveIterable<T> implements Iterable<T> {

    T e;
    boolean includeOriginal;

    public RecursiveIterable(T e) {
        this.e = e;
        includeOriginal = false;
    }

    public RecursiveIterable(boolean includeOriginal, T e) {
        this.e = e;
        this.includeOriginal = includeOriginal;
    }
}
