package org.entity3;

import com.google.common.collect.Iterables;
import org.entity3.hierarchical.Hierarchicals;

import java.io.Serializable;
import java.util.List;

/**
 * Created by home on 21.03.17.
 */
public interface IChild<T extends IChild<T,ID>,ID extends Serializable> extends IIdable<ID> {
    List<T> getParents();

    default boolean isCanHaveParents() {
        return true;
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

    default boolean isChildOfId(Iterable<ID> parentIds) {
        return Hierarchicals.isChildOfId((IChild)this, parentIds);
    }

    default boolean isChildOfId(ID... parentIds) {
        return Hierarchicals.isChildOfId((IChild)this, parentIds);
    }
    default T getFirstParent() {
        return Iterables.getFirst(getParents(), (T) this);
    }
    default void setParents(List<T> childs) {
        throw new UnsupportedOperationException(getClass().getName() + " method setParents not supported");
    }

}
