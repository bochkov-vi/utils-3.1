/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import com.google.common.collect.ImmutableList;

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


    public RecursiveIterator(IteratorExtractor<T> extractor, boolean includeOriginal, T rootEntity) {
        this.extractor = extractor;
        if (includeOriginal)
            iterators.add(ImmutableList.of(rootEntity).iterator());
        Iterator<T> iterator = extractor.extractIterator(rootEntity);
        if (iterator != null)
            iterators.add(iterator);

        this.rootEntity = rootEntity;
    }


    public boolean hasNext() {
        return getCurrentIterator().hasNext();
    }

    public T next() {
        next = getCurrentIterator().next();
        if (!Objects.equals(rootEntity, next)) {
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
