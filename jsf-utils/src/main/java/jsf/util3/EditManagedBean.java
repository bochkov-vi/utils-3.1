/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import jsf.util3.service.impl.JsfEntityServiceImpl;
import org.entity3.IIdable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Persistable;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jsf.util3.JsfUtil.addErrorMessage;
import static jsf.util3.JsfUtil.addInfoMessage;

/**
 * @param <T>
 * @author viktor
 */
public abstract class EditManagedBean<T extends IIdable<ID> & Persistable<ID>, ID extends Serializable> extends JsfEntityServiceImpl<T, ID> implements IEditManagedBean<T, ID> {


    protected T selected;

//    protected String editOutcome = "edit";

//    protected String saveOutcome = "edit";


    //protected String createOutcome = "edit";


    public EditManagedBean() {
    }

    public EditManagedBean(String... maskedProperty) {
        super(maskedProperty);
    }

    public EditManagedBean(String idParameterName, Iterable<String> maskedProperty) {
        super(idParameterName, maskedProperty);
    }

    @Override
    public String prepareCreate() {
        selected = null;
        return getToCreateOutcome();
    }

    @Override
    public String prepareEdit(T entity) {
        selected = entity;
        return getToEditOutcome();
    }

    @Override
    public String prepareDelete(T entity) {
        selected = entity;
        return null;
    }

    @Override
    @Transactional
    public String delete() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (selected != null) {
            try {
                getRepository().delete(selected);
                selected = null;
            } catch (Exception e) {
                JsfUtil.addErrorMessage( messageSource.getMessage(ERROR_ON_SAVE,null, LocaleContextHolder.getLocale()), ((NestedRuntimeException) e).getRootCause());
                return null;
            }
        } else {
            addErrorMessage(messageSource.getMessage(ERROR_ON_EMPTY,null, LocaleContextHolder.getLocale()));
        }
        return getAfterDeleteOutcome();
    }

    
    
    @Override
    @Transactional
    public String save() {
        boolean isNew = selected.isNew();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            selected = getRepository().saveAndFlush(selected);
            addInfoMessage(MessageFormat.format("{0}{1}",messageSource.getMessage(INFO_ON_SAVE,null, LocaleContextHolder.getLocale()), selected.getId()));
        } catch (NestedRuntimeException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(messageSource.getMessage(ERROR_ON_SAVE,null, LocaleContextHolder.getLocale()), e.getRootCause());
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(messageSource.getMessage(ERROR_ON_SAVE,null, LocaleContextHolder.getLocale()), MoreObjects.firstNonNull(e.getCause(), e));
            return null;
        }
        Map<String,String>params = ImmutableMap.of("faces-redirect","true",idParameterName,stringFromId(selected.getId()));
        String outcome = isNew ? getAfterCreateOutcome(params) : getAfterEditOutcome(params);
        return outcome;
    }

    @Override
    public T getSelected() {

        if (selected == null) {
            selected = entityFromRequest();
        }
        if (selected == null) {
            return selected = createNewInstance();
        }
        return selected;
    }

    @Override
    public void setSelected(T selected) {
        this.selected = selected;
    }

    @Override
    public String getToListOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "list");
    }

    @Override
    public String getAfterDeleteOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "list");
    }

    @Override
    public String getToEditOutcome() {
        return "edit";
    }

    @Override
    public String getAfterEditOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "edit");
    }

    @Override
    public String getAfterCreateOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "edit");
    }

    @Override
    public String getToCreateOutcome() {
        return "edit";
    }


    @Override
    public String getToListOutcome(Map<String, String> params) {
        return joinOutcome(getToListOutcome(), params);
    }

    @Override
    public String getAfterDeleteOutcome(Map<String, String> params) {
        return joinOutcome(getAfterDeleteOutcome(), params);
    }

    @Override
    public String getToEditOutcome(Map<String, String> params) {
        return joinOutcome(getToEditOutcome(), params);
    }

    @Override
    public String getAfterEditOutcome(Map<String, String> params) {
        return joinOutcome(getAfterEditOutcome(), params);
    }

    @Override
    public String getAfterCreateOutcome(Map<String, String> params) {
        return joinOutcome(getAfterCreateOutcome(), params);
    }

    @Override
    public String getToCreateOutcome(Map<String, String> params) {
        return joinOutcome(getToCreateOutcome(), params);
    }


    public String getParamOutcome() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String outcome = fc.getExternalContext().getRequestParameterMap().get("outcome");
        return outcome;
    }

    String joinOutcome(String outcome, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            if (outcome.contains("?")) {
                outcome = outcome + "&" + Joiner.on('&').join(Iterables.transform(params.entrySet(), e -> e.getKey() + "=" + e.getValue()));
            } else {
                outcome = outcome + "?" + Joiner.on('&').join(Iterables.transform(params.entrySet(), e -> e.getKey() + "=" + e.getValue()));
            }
        }
        return outcome;
    }


}
