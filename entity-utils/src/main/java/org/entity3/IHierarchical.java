/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3;

import java.io.Serializable;

/**
 * @param <T>
 * @author bochkov
 */
public interface IHierarchical<ID extends Serializable, T extends IHierarchical<ID, T>> extends IParent<T, ID>, IChild<T, ID> {
    default boolean isRealativeOf(Iterable<T> childs) {
        return isParentOf(childs) || isChildOf(childs);
    }

    default boolean isRealativeOf(T... childs) {
        return isParentOf(childs) || isChildOf(childs);
    }

    //==========================



   /* default boolean isRealativeOfId(Iterable<ID> childs) {
        return isParentOfId(childs) || isChildOfId(childs);
    }

    default boolean isRealativeOfId(ID... childs) {
        return isParentOfId(childs) || isChildOfId(childs);
    }*/


}
