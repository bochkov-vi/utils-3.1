/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.hierarchical;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.entity3.IChild;
import org.entity3.IExtractorId;
import org.entity3.IParent;

import java.io.Serializable;
import java.util.List;

/**
 * @author bochkov
 */
public class Hierarchicals {

    public static <ID extends Serializable, C extends IChild<C, ID>> boolean isChildOfId(final C child, Iterable<ID> parentIds) {
        Iterable<C> iterable = new ParentIterable<C>(child);
        return Iterables.any(iterable, input -> Iterables.contains(parentIds, input.getId()));
    }

    public static <ID extends Serializable, P extends IParent<P, ID>> boolean isParentOfId(final P parent, Iterable<ID> childIds) {
        Iterable<P> iterable = new ChildIterable<P>(parent);
        return Iterables.any(iterable, input -> Iterables.contains(childIds, input.getId()));
    }

    public static <ID extends Serializable, C extends IChild<C, ID>> boolean isChildOfId(final C child, ID... parentIds) {
        return isChildOfId(child, new ImmutableList.Builder().add((Object[]) parentIds).build());
    }

    public static <ID extends Serializable, C extends IParent<C, ID>> boolean isParentOfId(final C parent, ID... childIds) {
        return isParentOfId(parent, Lists.newArrayList(childIds));
    }

    public static <C extends IChild<C, ?>> boolean isChildOf(C child, final Iterable<C> parents) {
        Iterable<C> iterable = new ParentIterable<C>(child);
        return Iterables.any(iterable, t -> parents != null && Iterables.contains(parents, t));
    }

    public static <C extends IChild<C, ?>> boolean isChildOf(C child, C... parents) {
        return isChildOf(child, Lists.newArrayList(parents));
    }

    public static <P extends IParent<P, ?>> boolean isParentOf(P parent, final Iterable<P> childs) {
        Iterable<P> iterable = new ChildIterable<P>(parent);
        return Iterables.any(iterable, t -> childs != null && Iterables.contains(childs, t));

    }

    public static <P extends IParent<P, ?>> boolean isParentOf(P parent, P... childs) {
        return isParentOf(parent, Lists.newArrayList(childs));
    }

    public static <P extends IParent> List<P> getAllChilds(Iterable<P> parents) {
        return Lists.newArrayList(ChildIterable.create(parents));
    }

    public static <H extends IParent> List<H> getAllChilds(H... parents) {
        return getAllChilds(Lists.newArrayList(parents));
    }

    public static <C extends IChild<C, ?>> List<C> getAllParents(Iterable<C> childs) {
        return Lists.newArrayList(ImmutableSet.copyOf(ParentIterable.create(childs)));
    }

    public static <C extends IChild<C, ?>> List<C> getAllParents(C... childs) {
        return getAllParents(Lists.newArrayList(childs));
    }

    public static <H extends IParent<H, ID>, ID extends Serializable> Iterable<ID> getAllChildIds(Iterable<H> parent) {
        return IExtractorId.extractIds(ChildIterable.create(parent));
    }

    public static <H extends IChild<H, ID>, ID extends Serializable> Iterable<ID> getAllParentIds(Iterable<H> child) {
        return IExtractorId.extractIds(ParentIterable.create(child));
    }

    public static <H extends IParent<H, ID>, ID extends Serializable> Iterable<ID> getAllChildIds(H... parent) {
        return IExtractorId.extractIds(ChildIterable.create(parent));
    }

    public static <H extends IChild<H, ID>, ID extends Serializable> Iterable<ID> getAllParentIds(H... child) {
        return IExtractorId.extractIds(ParentIterable.create(child));
    }

    public static <H extends IParent<H, ID>, ID extends Serializable> Iterable<ID> getAllChildIds(boolean includeOriginal, Iterable<H> parent) {
        return IExtractorId.extractIds(ChildIterable.create(includeOriginal, parent));
    }

    public static <H extends IChild<H, ID>, ID extends Serializable> Iterable<ID> getAllParentIds(boolean includeOriginal, Iterable<H> child) {
        return IExtractorId.extractIds(ParentIterable.create(includeOriginal, child));
    }

    public static <H extends IParent<H, ID>, ID extends Serializable> Iterable<ID> getAllChildIds(boolean includeOriginal, H... parent) {
        return IExtractorId.extractIds(ChildIterable.create(includeOriginal, parent));
    }

    public static <H extends IChild<H, ID>, ID extends Serializable> Iterable<ID> getAllParentIds(boolean includeOriginal, H... child) {
        return IExtractorId.extractIds(ParentIterable.create(includeOriginal, child));
    }

    public static <H extends IParent<H, ?>> List<H> getAllChilds(boolean includeOriginal, Iterable<H> parents) {
        return Lists.newArrayList(ImmutableSet.copyOf(ChildIterable.create(includeOriginal, parents)));
    }

    public static <H extends IParent<H, ?>> List<H> getAllChilds(boolean includeOriginal, H... parents) {
        return getAllChilds(includeOriginal, Lists.newArrayList(parents));
    }

    public static <C extends IChild<C, ?>> List<C> getAllParents(boolean includeOriginal, Iterable<C> childs) {
        return Lists.newArrayList(ImmutableSet.copyOf(ParentIterable.create(includeOriginal, childs)));
    }

    public static <C extends IChild<C, ?>> List<C> getAllParents(boolean includeOriginal, C... childs) {
        return getAllParents(includeOriginal, Lists.newArrayList(childs));
    }

}
