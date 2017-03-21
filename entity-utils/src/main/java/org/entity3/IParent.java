package org.entity3;

import org.entity3.hierarchical.Hierarchicals;

import java.io.Serializable;
import java.util.List;

/**
 * Created by home on 21.03.17.
 */
public interface IParent<T extends IParent<T,ID>,ID extends Serializable> extends IIdable<ID> {
    List<T> getChilds();
    default boolean isCanHaveChilds() {
        return true;
    }
    default boolean isParentOf(Iterable<T> childs) {
        return Hierarchicals.isParentOf((T) this, childs);
    }

    default boolean isParentOf(T... childs) {
        return Hierarchicals.isParentOf((T) this, childs);
    }
    default List<T> getAllChilds() {
        return Hierarchicals.getAllChilds((T) this);
    }

    default List<T> getAllChildsAndThis() {
        return Hierarchicals.getAllChilds(true, (T) this);
    }
    default void setChilds(List<T> parents) {
        throw new UnsupportedOperationException(getClass().getName() + " method setChilds not supported");
    }
    default boolean isParentOfId(Iterable<ID> childIds) {
        return Hierarchicals.isParentOfId((IParent)this, childIds);
    }
    default boolean isParentOfId(ID... childIds) {
        return Hierarchicals.isParentOfId((IParent)this, childIds);
    }


}
