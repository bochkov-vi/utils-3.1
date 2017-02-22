/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import org.entity3.IHierarchical;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @param <T>
 * @author bochkov
 */
public class RecursiveIterator<T extends IHierarchical> implements Iterator<T> {

    List<Iterator<T>> iterators = new LinkedList<Iterator<T>>();
    Iterator<T> current;
    IteratorExtractor<T> extractor;

    public RecursiveIterator(IteratorExtractor<T> extractor, boolean includeOriginal, Iterable<T> iterable) {
        this.extractor = extractor;
        if (includeOriginal) {
            iterators.add(iterable.iterator());
        } else {
            for (T e : iterable) {
                iterators.add(extractor.extractIterator(e));
            }
        }
    }

    public RecursiveIterator(IteratorExtractor<T> extractor, boolean includeOriginal, T... iterable) {
        this(extractor, includeOriginal, Lists.newArrayList(iterable));
    }

    public boolean hasNext() {
        return getCurrent().hasNext();
    }

    public T next() {
        T next = getCurrent().next();
        Iterator<T> nextIterator = extractor.extractIterator(next);
        if (nextIterator != null) {
            iterators.add(nextIterator);
        }
        return next;
    }

    public void remove() {
        throw new UnsupportedOperationException(); //To change body of generated methods, choose Tools | Templates.
    }

    Iterator<T> getCurrent() {
        while ((current == null || !current.hasNext()) && !iterators.isEmpty()) {
            current = iterators.remove(0);
        }
        if (current == null) {
            current = Collections.emptyIterator();
        }
        return current;
    }
}
