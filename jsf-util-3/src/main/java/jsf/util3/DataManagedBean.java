/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3;


import com.google.common.collect.ImmutableList;
import org.entity3.IIdable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @param <T>
 * @author viktor
 */
public abstract class DataManagedBean<T extends IIdable> extends LazyDataModel<T> implements Serializable {

    protected DataManagedBean() {
        this.setPageSize(50);
    }

    public abstract JpaSpecificationExecutor<T> getRepository();

    @Override
    public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        Specification<T> filter = Specifications.where(getSpecification(filters));
        Pageable pageRequest = new PageRequest(first / pageSize, pageSize, getSort(multiSortMeta));
        Page page = getRepository().findAll(filter, pageRequest);
        setRowCount((int) page.getTotalElements());
        return page.getContent();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        SortMeta sm = new SortMeta();
        sm.setSortField(sortField);
        sm.setSortOrder(sortOrder);
        return load(first, pageSize, ImmutableList.of(sm), filters);
    }

    protected abstract Specification<T> getSpecification(Map<String, Object> filters);

    public Sort getSort(List<SortMeta> multiSortMeta) {
        Sort sort = null;
        if (multiSortMeta != null) {
            for (SortMeta meta : multiSortMeta) {
                String field = meta.getSortField();
                Sort.Direction order = SortOrder.DESCENDING == meta.getSortOrder() ? Sort.Direction.DESC : Sort.Direction.ASC;
                if (order != null && field != null) {
                    if (sort == null) {
                        sort = new Sort(order, field);
                    } else {
                        sort = sort.and(new Sort(order, field));
                    }
                }
            }
        }
        return sort;
    }

}
