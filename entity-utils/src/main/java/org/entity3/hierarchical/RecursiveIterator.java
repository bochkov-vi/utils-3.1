/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import java.util.*;

/**
 * @param <T>
 * @author bochkov
 */
public class RecursiveIterator<T> implements Iterator<T> {

    List<Iterator<T>> iterators = new LinkedList<Iterator<T>>();
    Iterator<T> currentIterator;
    IteratorExtractor<T> extractor;
    T rootEntity;
    T next = null;
    boolean includeOriginal;

    public RecursiveIterator(IteratorExtractor<T> extractor, boolean includeOriginal, T rootEntity) {
        this.extractor = extractor;
        iterators.add(extractor.extractIterator(rootEntity));
        this.includeOriginal = includeOriginal;
        this.rootEntity = rootEntity;
    }


    public boolean hasNext() {
        return getCurrentIterator().hasNext();
    }

    public T next() {
        if (next == null && includeOriginal) {
            next = rootEntity;
        } else {
            next = getCurrentIterator().next();
            if (Objects.equals(rootEntity, next)) {
                next = null;
            }
            Iterator<T> nextIterator = extractor.extractIterator(next);
            if (nextIterator != null) {
                iterators.add(nextIterator);
            }
        }
        return next;
    }

    public void remove() {
        throw new UnsupportedOperationException(); //To change body of generated methods, choose Tools | Templates.
    }

    Iterator<T> getCurrentIterator() {
        while ((currentIterator == null || !currentIterator.hasNext()) && !iterators.isEmpty()) {
            currentIterator = iterators.remove(0);
        }
        if (currentIterator == null) {
            currentIterator = Collections.emptyIterator();
        }
        return currentIterator;
    }
}
