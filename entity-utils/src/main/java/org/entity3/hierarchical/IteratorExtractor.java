/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.entity3.hierarchical;

import java.util.Iterator;

/**
 * @param <T>
 * @author bochkov
 */
public interface IteratorExtractor<T> {
    Iterator<T> extractIterator(T e);

}
