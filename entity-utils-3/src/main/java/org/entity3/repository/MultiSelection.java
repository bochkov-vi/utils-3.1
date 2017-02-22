package org.entity3.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * Created by bochkov on 29.08.2014.
 */
public interface MultiSelection<P> {
    Iterable<Selection<P>> toSelection(Root root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
