/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entity3.repository;

import org.entity3.IGetNamed;

import java.util.List;

/**
 * @param <T>
 * @author bochkov
 */
public interface UniqueNamedRepository<T extends IGetNamed> {

    T findByName(String name);

    List<T> findByNameStartingWith(String name);
}
