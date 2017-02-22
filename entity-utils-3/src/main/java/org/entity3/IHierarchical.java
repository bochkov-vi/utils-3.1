/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3;

import com.google.common.collect.Iterables;
import org.entity3.hierarchical.Hierarchicals;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @author bochkov
 */
public interface IHierarchical<ID extends Serializable, T extends IHierarchical> extends IIdable<ID>{

    List<T> getChildList();

    List<T> getParentList();

    default boolean isCanHaveChilds() {
        return true;
    }

    default boolean isCanHaveParents() {
        return true;
    }

    default List<T> getAllChilds() {
        return Hierarchicals.getAllChilds((T) this);
    }

    default List<T> getAllChildsAndThis() {
        return Hierarchicals.getAllChilds(true, (T) this);
    }

    default List<T> getAllParentsAndThis() {
        return Hierarchicals.getAllParents(true, (T) this);
    }

    default List<T> getAllParents() {
        return Hierarchicals.getAllParents((T) this);
    }

    default boolean isChildOf(Iterable<T> parents) {
        return Hierarchicals.isChildOf((T) this, parents);
    }

    default boolean isChildOf(T... parents) {
        return Hierarchicals.isChildOf((T) this, parents);
    }


    default boolean isParentOf(Iterable<T> childs) {
        return Hierarchicals.isParentOf((T) this, childs);
    }

    default boolean isParentOf(T... childs) {
        return Hierarchicals.isParentOf((T) this, childs);
    }

    default boolean isRealativeOf(Iterable<T> childs) {
        return isParentOf(childs) || isChildOf(childs);
    }

    default boolean isRealativeOf(T... childs) {
        return isParentOf(childs) || isChildOf(childs);
    }

    //==========================
    default boolean isChildOfId(Iterable<ID> parentIds) {
        return Hierarchicals.isChildOfId(this, parentIds);
    }

    default boolean isChildOfId(ID... parentIds) {
        return Hierarchicals.isChildOfId(this, parentIds);
    }

    default boolean isParentOfId(Iterable<ID> childIds) {
        return Hierarchicals.isParentOfId(this, childIds);
    }

    default boolean isParentOfId(ID... childIds) {
        return Hierarchicals.isParentOfId(this, childIds);
    }

    default boolean isRealativeOfId(Iterable<ID> childs) {
        return isParentOfId(childs) || isChildOfId(childs);
    }

    default boolean isRealativeOfId(ID... childs) {
        return isParentOfId(childs) || isChildOfId(childs);
    }

    default T getFirstParent() {
        return Iterables.getFirst(getParentList(), (T) this);
    }

    default void setParentList(List<T> parentList) {
        throw new UnsupportedOperationException(getClass().getName() + " method setParentList not supported");
    }

    default void setChildList(List<T> childList) {
        throw new UnsupportedOperationException(getClass().getName() + " method setChildList not supported");
    }

}
