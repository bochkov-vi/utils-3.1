/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.entity3.IParent;

import java.util.Iterator;

/**
 * @param <T>
 * @author bochkov
 */
public class ChildIterable<T extends IParent<T,?>> extends RecursiveIterable<T> {

    public ChildIterable(T e) {
        super(e);
    }

    public ChildIterable(boolean includeOriginal, T e) {
        super(includeOriginal, e);
    }

    public static <T extends IParent<T,?>> Iterable<T> create(Iterable<T> iterable) {
        return Iterables.concat(Iterables.transform(iterable, e -> new ChildIterable<T>(e)));
    }

    public static <T extends IParent<T,?>> Iterable<T> create(T... iterable) {
        return Iterables.concat(Iterables.transform(Lists.newArrayList(iterable), e -> new ChildIterable<T>(e)));
    }

    public static <T extends IParent<T,?>> Iterable<T> create(boolean includeOriginal, Iterable<T> iterable) {
        return Iterables.concat(Iterables.transform(iterable, e -> new ChildIterable<T>(includeOriginal, e)));
    }

    public static <T extends IParent<T,?>> Iterable<T> create(boolean includeOriginal, T... iterable) {
        return Iterables.concat(Iterables.transform(Lists.newArrayList(iterable), e -> new ChildIterable<T>(includeOriginal, e)));
    }

    public Iterator<T> iterator() {
        return new ChildIterator<T>(includeOriginal, this.e);
    }
}
