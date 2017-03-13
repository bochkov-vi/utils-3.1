/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.service;

import org.entity3.repository.CustomRepository;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @param <ID>
 * @author viktor
 */


public interface EntityService<T, ID extends Serializable>  extends CustomRepository<T,ID> {
    public List<T> findByMask(String mask);

    public List<ID> findIdByMask(String mask);

    public <P> List<P> findPropertyByMask(String property, String mask);

    public <P> List<P> findPropertyByMask(String propertyPath, String mask, Specification additionSpecification);

    public <P> List<P> findPropertyByMask(String propertyPath, String mask, String... maskedProperties);

    public <P> List<P> findPropertyByMask(String propertyPath, String mask, Specification additionSpecification, String... maskedProperties);

}
