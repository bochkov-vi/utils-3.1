/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import org.entity3.IParent;

import java.util.Iterator;

/**
 * @param <T>
 * @author bochkov
 */
public class ChildIteratorExtractor<T extends IParent> implements IteratorExtractor<T> {


    public Iterator<T> extractIterator(T e) {
        if (e != null && e.getChilds() != null && !e.getChilds().isEmpty() && e.isCanHaveChilds()) {
            return e.getChilds().iterator();
        } else {
            return null;
        }
    }
}
