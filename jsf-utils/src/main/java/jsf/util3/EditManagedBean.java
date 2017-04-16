/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.entity3.IIdable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Persistable;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jsf.util3.JsfUtil.addErrorMessage;
import static jsf.util3.JsfUtil.addInfoMessage;

/**
 * @param <T>
 * @author viktor
 */
public abstract class EditManagedBean<T extends Persistable<ID> & IIdable<ID>, ID extends Serializable> extends ManagedConverter<T, ID> {

    public static final String ERROR_ON_SAVE = "errorOnSave";

    public static final String ERROR_ON_DELETE = "errorOnDelete";

    public static final String ERROR_ON_EMPTY = "errorOnEmptySelected";

    public static final String INFO_ON_SAVE = "infoOnObjectSave";

    public static final String INFO_ON_OBJECT_EXISTS = "infoOnObjectExists";


    protected T selected;

//    protected String editOutcome = "edit";

//    protected String saveOutcome = "edit";


    //protected String createOutcome = "edit";

    protected String idParameterName = "id";

    @Autowired
    @Qualifier("jsf-util-messages")
    protected Properties msg;

    protected EditManagedBean(String idParameterName) {
        this.idParameterName = idParameterName;
    }

    protected EditManagedBean(Class<T> entityClass, Class<ID> idClass, String idParameterName) {
        super(entityClass, idClass);
        this.idParameterName = idParameterName;
    }

    protected EditManagedBean(Class<T> entityClass, String idParameterName) {
        super(entityClass);
        this.idParameterName = idParameterName;
    }

    protected EditManagedBean(Class<T> entityClass, Class<ID> idClass) {
        super(entityClass, idClass);
        idParameterName = entityClass.getSimpleName();
    }

    protected EditManagedBean(Class<T> entityClass) {
        super(entityClass);
        idParameterName = entityClass.getSimpleName();
    }

    protected EditManagedBean() {
        super();
        idParameterName = entityClass.getSimpleName();
    }


    public String prepareCreate() {
        selected = null;
        return getToCreateOutcome();
    }

    public String prepareEdit(T entity) {
        selected = entity;
        return getToEditOutcome();
    }

    public String prepareDelete(T entity) {
        selected = entity;
        return null;
    }

    public String delete() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (selected != null) {
            try {
                getRepository().delete(selected);
                selected = null;
            } catch (Exception e) {
                JsfUtil.addErrorMessage(msg.getProperty(ERROR_ON_DELETE), ((NestedRuntimeException) e).getRootCause());
                return null;
            }
        } else {
            addErrorMessage(msg.getProperty(ERROR_ON_EMPTY));
        }
        return getAfterDeleteOutcome();
    }

    public String save() {
        boolean isNew = selected.isNew();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            selected = getRepository().saveAndFlush(selected);
            addInfoMessage(MessageFormat.format("{0}{1}", msg.getProperty(INFO_ON_SAVE), selected.getId()));
        } catch (NestedRuntimeException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(msg.getProperty(ERROR_ON_SAVE), e.getRootCause());
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(msg.getProperty(ERROR_ON_SAVE), MoreObjects.firstNonNull(e.getCause(), e));
            return null;
        }
        Map<String,String>params = ImmutableMap.of("faces-redirect","true",idParameterName,stringFromId(selected.getId()));
        String outcome = isNew ? getAfterCreateOutcome(params) : getAfterEditOutcome(params);
        return outcome;
    }

    public T getSelected() {

        if (selected == null) {
            selected = entityFromRequest();
        }
        if (selected == null) {
            return selected = createNewInstance();
        }
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public T entityFromRequest(String parameterName) {
        T result = null;
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            String idStr = fc.getExternalContext().getRequestParameterMap().get(parameterName);
            if (!Strings.isNullOrEmpty(idStr)) {
                result = this.convert(idStr);
            }
        }
        return result;
    }

    public T entityFromRequest() {
        return entityFromRequest(idParameterName);
    }

    public T createNewInstance() {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(EditManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EditManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getToListOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "list");
    }

    public String getAfterDeleteOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "list");
    }

    public String getToEditOutcome() {
        return "edit";
    }

    public String getAfterEditOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "edit");
    }

    public String getAfterCreateOutcome() {
        return MoreObjects.firstNonNull(getParamOutcome(), "edit");
    }

    public String getToCreateOutcome() {
        return "edit";
    }


    public String getToListOutcome(Map<String, String> params) {
        return joinOutcome(getToListOutcome(), params);
    }

    public String getAfterDeleteOutcome(Map<String, String> params) {
        return joinOutcome(getAfterDeleteOutcome(), params);
    }

    public String getToEditOutcome(Map<String, String> params) {
        return joinOutcome(getToEditOutcome(), params);
    }

    public String getAfterEditOutcome(Map<String, String> params) {
        return joinOutcome(getAfterEditOutcome(), params);
    }

    public String getAfterCreateOutcome(Map<String, String> params) {
        return joinOutcome(getAfterCreateOutcome(), params);
    }

    public String getToCreateOutcome(Map<String, String> params) {
        return joinOutcome(getToCreateOutcome(), params);
    }


    public String getParamOutcome() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String outcome = fc.getExternalContext().getRequestParameterMap().get("outcome");
        return outcome;
    }

    String joinOutcome(String outcome, Map<String, String> params) {
        if (params != null && params.isEmpty()) {
            if (outcome.contains("?")) {
                outcome = outcome + "&" + Joiner.on('&').join(Iterables.transform(params.entrySet(), e -> e.getKey() + "=" + e.getValue()));
            } else {
                outcome = outcome + "?" + Joiner.on('&').join(Iterables.transform(params.entrySet(), e -> e.getKey() + "=" + e.getValue()));
            }
        }
        return outcome;
    }

    public String getIdParameterName() {
        return idParameterName;
    }


}
