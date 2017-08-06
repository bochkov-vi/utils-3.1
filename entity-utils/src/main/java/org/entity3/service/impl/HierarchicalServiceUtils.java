package org.entity3.service.impl;

import org.entity3.IHierarchical;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;

public class HierarchicalServiceUtils extends EntityServiceUtils{

    public static <T extends IHierarchical> Specification<T> createEmptyChildsSpecification() {
        return (root, query, cb) -> {
            Expression e = root.get("childs");
            return cb.isEmpty(e);
        };
    }

    public static  <T extends IHierarchical> Specification<T> createEmptyParentsSpecification() {
        return (root, query, cb) -> {
            Expression e = root.get("parents");
            return cb.isEmpty(e);
        };
    }
}
