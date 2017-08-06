package org.entity3.service.impl;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import org.entity3.repository.PropertySelection;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

public class EntityServiceUtils {
    public static Path createPath(From root, String path, Collection<Path> pathCache) {
        Path result = root;
        for (String attributeName : path.split("\\.")) {
            if (attributeName.length() > 0) {
                if (Persistable.class.isAssignableFrom(result.get(attributeName).getJavaType())) {
                    result = root.join(attributeName, JoinType.LEFT);
                } else {
                    result = result.get(attributeName);
                }
                if (pathCache != null) {
                    boolean addToCache = true;
                    for (Path p : pathCache) {
                        if (p.getModel().equals(result.getModel())) {
                            result = p;
                            addToCache = false;
                            break;
                        }
                    }
                    if (addToCache) {
                        pathCache.add(result);
                    }
                }
            }
        }
        return result;
    }

    static public Predicate createStringMaskExpression(String mask, Expression maskedProperty, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.like(maskedProperty.as(String.class), "%" + mask.trim() + "%");
    }

    static protected Predicate createMaskExpression(String mask, Expression maskedProperty, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return createStringMaskExpression(mask, maskedProperty, query, cb);
    }

    static public <T> Specification<T> createFindByMaskSpecification(final String mask, final String maskedPopertyName, final Collection<Path> pathCache) {
        return (root, query, cb) -> {
            Predicate result;
            Path maskedProperty = createPath(root, maskedPopertyName, pathCache);
            result = createMaskExpression(mask, maskedProperty, query, cb);
            if (result != null) {
                List<Order> orders;
                if (query.getOrderList() == null) {
                    orders = ImmutableList.of();
                } else {
                    orders = ImmutableList.copyOf(query.getOrderList());
                }
                orders = ImmutableList.<Order>builder().add(cb.asc(cb.locate(maskedProperty, mask)), cb.asc(maskedProperty.isNull()), cb.asc(cb.length(maskedProperty)), cb.asc(maskedProperty)).addAll(orders).build();
                query.orderBy(orders);
            }
            return result;
        };
    }

    static public <T> Specification<T> createFindByMaskSpecification(final String mask, Iterable<String> maskedPoperties, Collection<Path> pathCashe) {
        Specifications<T> where = null;
        for (String propertyName : MoreObjects.firstNonNull(maskedPoperties, ImmutableList.<String>of())) {
            Specification<T> spec = createFindByMaskSpecification(mask, propertyName, pathCashe);
            if (where == null) {
                where = Specifications.where(spec);
            } else {
                where = where.or(spec);
            }
        }
        return where;
    }

    static public <P> PropertySelection<P> createPropertySelection(final String propertyPath, final Collection<Path> pathCashe) {
        return createPropertySelection(propertyPath, true, pathCashe);
    }

    static public <P> PropertySelection<P> createPropertySelection(final String propertyPath, boolean distinct, final Collection<Path> pathCashe) {
        return (root, query, cb) -> {
            query.distinct(distinct);
            return createPath(root, propertyPath, pathCashe);
        };
    }
}
