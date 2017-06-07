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
import java.io.IOException;
import java.io.ObjectOutputStream;
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
public abstract class SerializaedEditor<T extends Persistable<ID> & IIdable<ID> & Serializable, ID extends Serializable> extends ManagedConverter<T, ID> implements Serializable {

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

    protected SerializaedEditor(String idParameterName) {
        this.idParameterName = idParameterName;
    }

    protected SerializaedEditor(Class<T> entityClass, Class<ID> idClass, String idParameterName) {
        super(entityClass, idClass);
        this.idParameterName = idParameterName;
    }

    protected SerializaedEditor(Class<T> entityClass, String idParameterName) {
        super(entityClass);
        this.idParameterName = idParameterName;
    }

    protected SerializaedEditor(Class<T> entityClass, Class<ID> idClass) {
        super(entityClass, idClass);
        idParameterName = entityClass.getSimpleName();
    }

    protected SerializaedEditor(Class<T> entityClass) {
        super(entityClass);
        idParameterName = entityClass.getSimpleName();
    }

    protected SerializaedEditor() {
        super();
        idParameterName = entityClass.getSimpleName();
    }


    public String prepareCreate() {
        setSelected(null);
        return getToCreateAction();
    }

    public String prepareEdit(T entity) {
        selected = entity;
        return getToEditAction();
    }

    public String prepareDelete(T entity) {
        selected = entity;
        return null;
    }

    public String delete() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (selected != null && !selected.isNew()) {
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
        return getAfterDeleteAction();
    }

    public String save() {

        boolean isNew = getSelected().isNew();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            setSelected(getRepository().saveAndFlush(getSelected()));
            addInfoMessage(MessageFormat.format("{0}{1}", msg.getProperty(INFO_ON_SAVE), getSelected().getId()));
        } catch (NestedRuntimeException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(msg.getProperty(ERROR_ON_SAVE), e.getRootCause());
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(msg.getProperty(ERROR_ON_SAVE), MoreObjects.firstNonNull(e.getCause(), e));
            return null;
        }
        String outcome = isNew ? getAfterCreateAction() : getAfterEditAction();
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
            Logger.getLogger(SerializaedEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SerializaedEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getToListAction() {
        return "list";
    }

    public String getAfterDeleteAction() {
        return getToListAction();
    }

    public String getToDeleteAction() {
        return "delete";
    }

    public String getToEditAction() {
        return "edit";
    }

    public String getCancelEditAction() {
        return "cancel";
    }

    public String getAfterEditAction() {
        return getToListAction();
    }

    public String getAfterCreateAction() {
        return getAfterEditAction();
    }

    public String getToCreateAction() {
        return getToEditAction();
    }

    public String getIdParameterName() {
        return idParameterName;
    }


}
