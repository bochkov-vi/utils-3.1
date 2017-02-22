/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.entity3.IExtractorId;
import org.entity3.IHierarchical;

import java.io.Serializable;
import java.util.List;

/**
 * @author bochkov
 */
public class Hierarchicals {

    public static <ID extends Serializable, H extends IHierarchical> boolean isChildOfId(final H child, final Iterable<ID> parentIds) {
        Iterable<H> iterable = new ParentIterable<H>(child);
        return Iterables.any(iterable, new Predicate<H>() {
            public boolean apply(H input) {
                return Iterables.contains(parentIds, input.getId());
            }
        });
    }

    public static <ID extends Serializable, H extends IHierarchical<ID,H>> boolean isParentOfId(final H parent, final Iterable<ID> childIds) {
        Iterable<H> iterable = new ChildIterable<H>(parent);
        return Iterables.any(iterable, new Predicate<H>() {
            public boolean apply(H input) {
                return Iterables.contains(childIds, input.getId());
            }
        });
    }

    public static <ID extends Serializable, H extends IHierarchical > boolean isChildOfId(final H child, final ID... parentIds) {
        return isChildOfId(child, new ImmutableList.Builder().add((Object[]) parentIds).build());
    }

    public static <ID, H extends IHierarchical> boolean isParentOfId(final H parent, final ID... childIds) {
        return isParentOfId(parent, new ImmutableList.Builder().add((Object[]) childIds).build());
    }

    public static <H extends IHierarchical> boolean isChildOf(H child, final Iterable<H> parents) {
        Iterable<H> iterable = new ParentIterable<H>(child);
        return Iterables.any(iterable, new Predicate<H>() {

            public boolean apply(H t) {
                return Iterables.contains(parents, t);
            }
        });
    }

    public static <H extends IHierarchical> boolean isChildOf(H child, H... parents) {
        return isChildOf(child, Lists.newArrayList(parents));
    }

    public static <H extends IHierarchical> boolean isParentOf(H parent, final Iterable<H> childs) {
        Iterable<H> iterable = new ParentIterable<H>(parent);
        return Iterables.any(iterable, new Predicate<H>() {

            public boolean apply(H t) {
                return Iterables.contains(childs, t);
            }
        });

    }

    public static <H extends IHierarchical> boolean isParentOf(H parent, H... childs) {
        return isParentOf(parent, Lists.newArrayList(childs));
    }

    public static <H extends IHierarchical> List<H> getAllChilds(Iterable<H> parents) {
        return Lists.newArrayList(new ChildIterable<H>(parents));
    }

    public static <H extends IHierarchical> List<H> getAllChilds(H... parents) {
        return getAllChilds(Lists.newArrayList(parents));
    }

    public static <H extends IHierarchical> List<H> getAllParents(Iterable<H> childs) {
        return Lists.newArrayList(ImmutableSet.copyOf(new ParentIterable<H>(childs)));
    }

    public static <H extends IHierarchical> List<H> getAllParents(H... childs) {
        return getAllParents(Lists.newArrayList(childs));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllChildIds(Iterable<H> parent) {
        return IExtractorId.extractIds(new ChildIterable<H>(parent));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllParentIds(Iterable<H> child) {
        return IExtractorId.extractIds(new ParentIterable<H>(child));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllChildIds(H... parent) {
        return IExtractorId.extractIds(new ChildIterable<H>(parent));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllParentIds(H... child) {
        return IExtractorId.extractIds(new ParentIterable<H>(child));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllChildIds(boolean includeOriginal, Iterable<H> parent) {
        return IExtractorId.extractIds(new ChildIterable<H>(includeOriginal, parent));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllParentIds(boolean includeOriginal, Iterable<H> child) {
        return IExtractorId.extractIds(new ParentIterable<H>(includeOriginal, child));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllChildIds(boolean includeOriginal, H... parent) {
        return IExtractorId.extractIds(new ChildIterable<H>(includeOriginal, parent));
    }

    public static <H extends IHierarchical<ID,H>, ID extends Serializable> Iterable<ID> getAllParentIds(boolean includeOriginal, H... child) {
        return IExtractorId.extractIds(new ParentIterable<H>(includeOriginal, child));
    }

    public static <H extends IHierarchical> List<H> getAllChilds(boolean includeOriginal, Iterable<H> parents) {
        return Lists.newArrayList(ImmutableSet.copyOf(new ChildIterable<H>(includeOriginal, parents)));
    }

    public static <H extends IHierarchical> List<H> getAllChilds(boolean includeOriginal, H... parents) {
        return getAllChilds(includeOriginal, Lists.newArrayList(parents));
    }

    public static <H extends IHierarchical> List<H> getAllParents(boolean includeOriginal, Iterable<H> childs) {
        return Lists.newArrayList(ImmutableSet.copyOf(new ParentIterable<H>(includeOriginal, childs)));
    }

    public static <H extends IHierarchical> List<H> getAllParents(boolean includeOriginal, H... childs) {
        return getAllParents(includeOriginal, Lists.newArrayList(childs));
    }

}
