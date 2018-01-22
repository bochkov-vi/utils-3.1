package jsf.util3.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import jsf.util3.JsfUtil;
import jsf.util3.service.JsfHierarchicalEntityService;
import org.entity3.IHierarchical;
import org.entity3.IIdable;
import org.entity3.service.impl.HierarchicalServiceUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specifications;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public abstract class JsfHierarchicalEntityServiceImpl<T extends IIdable<ID> & IHierarchical<ID, T>, ID extends Serializable> extends JsfEntityServiceImpl<T, ID> implements JsfHierarchicalEntityService<T, ID> {

    public JsfHierarchicalEntityServiceImpl() {
        super();
    }

    public JsfHierarchicalEntityServiceImpl(String... maskedProperty) {
        super(maskedProperty);
    }

    public JsfHierarchicalEntityServiceImpl(String idParameterName, Iterable<String> maskedProperty) {
        super(idParameterName, maskedProperty);
    }

    @Override
    public List<T> findByMaskAndEmptyChilds(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask, this.maskedPopertyArray, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyChildsSpecification()));
    }

    @Override
    public List<T> findByMaskAndEmptyParents(String mask) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask, this.maskedPopertyArray, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyParentsSpecification()));
    }

    @Override
    public List<T> findByEmptyChilds() {
        return findAll(HierarchicalServiceUtils.createEmptyChildsSpecification());
    }

    @Override
    public List<T> findByEmptyParents() {
        return findAll(HierarchicalServiceUtils.createEmptyParentsSpecification());
    }

    @Override
    public boolean validateCircleLink(Iterable<IHierarchical> childs, Iterable<IHierarchical> parents) {
        boolean result = childs != null && parents != null && Iterables.any(childs, c -> c.isParentOf(parents) || Iterables.contains(parents, c));
        if (result) {
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage(CIRCLE_ERROR_MESSAGE, null, locale);
            JsfUtil.addErrorMessage(message);
        }
        return !result;
    }
    @Override
    public List<T> findByMaskAndEmptyChilds(String mask, Integer limit) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask,this.maskedPopertyArray, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyChildsSpecification()),limit);
    }

    @Override
    public List<T> findByMaskAndEmptyParents(String mask, Integer limit) {
        return findAll(Specifications.where(createFindByMaskSpecification(mask,this.maskedPopertyArray, Lists.newArrayList()))
                .and(HierarchicalServiceUtils.<T>createEmptyParentsSpecification()),limit);
    }

    @Override
    public List<T> findByEmptyChilds(Integer limit) {
        return findAll(HierarchicalServiceUtils.createEmptyChildsSpecification(),limit);
    }

    @Override
    public List<T> findByEmptyParents(Integer limit) {
        return findAll(HierarchicalServiceUtils.createEmptyParentsSpecification(),limit);
    }
}
