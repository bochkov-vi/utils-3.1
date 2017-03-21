/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;

/**
 * @param <T>
 * @param <ID>
 * @author bochkov
 */
public class IExtractorId<T extends Persistable<ID>, ID extends Serializable> implements Function<T, ID> {
    public ID apply(T f) {
        return f.getId();
    }

    public static <T extends IIdable<ID>, ID extends Serializable> Collection<ID> extractIds(Iterable<T> iterable) {
        return Collections2.filter(Collections2.transform(ImmutableSet.copyOf(iterable), new Function<T, ID>() {

            public ID apply(T f) {
                return f.getId();
            }
        }), Predicates.notNull());
    }
}
