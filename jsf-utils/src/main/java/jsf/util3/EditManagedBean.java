/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3;

import com.google.common.base.Strings;
import org.entity3.IIdable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jsf.util3.JsfUtil.addErrorMessage;
import static jsf.util3.JsfUtil.addInfoMessage;

/**
 * @param <T>
 * @author viktor
 */
public abstract class EditManagedBean<T extends IIdable<ID>, ID extends Serializable> extends ManagedConverter<T, ID> {

    public static final String ERROR_ON_SAVE = "errorOnSave";

    public static final String ERROR_ON_DELETE = "errorOnDelete";

    public static final String ERROR_ON_EMPTY = "errorOnEmptySelected";

    public static final String INFO_ON_SAVE = "infoOnObjectSave";

    public static final String INFO_ON_OBJECT_EXISTS = "infoOnObjectExists";


    protected T selected;

    protected String editOutcome = "edit";

    protected String saveOutcome = "edit";

    protected String listOutcome = "list";

    protected String createOutcome = "edit";

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
    }

    protected EditManagedBean(Class<T> entityClass) {
        super(entityClass);
    }

    protected EditManagedBean() {
        super();
    }


    public String prepareCreate() {
        selected = null;
        return createOutcome;
    }

    public String prepareEdit(T entity) {
        selected = entity;
        return editOutcome;
    }

    public String prepareDelete(T entity) {
        selected = entity;
        return null;
    }

    public String delete() {
        if (selected != null) {
            try {
                getRepository().delete(selected);
                selected = null;
            } catch (Exception e) {
                JsfUtil.addErrorMessage(msg.getProperty(ERROR_ON_DELETE), e);
            }
        } else {
            addErrorMessage(msg.getProperty(ERROR_ON_EMPTY));
        }
        return listOutcome;
    }

    public String save() {
        try {
            selected = getRepository().saveAndFlush(selected);
            addInfoMessage(MessageFormat.format("{0}{1}", msg.getProperty(INFO_ON_SAVE), selected.getId()));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(msg.getProperty(ERROR_ON_SAVE), e);
            return null;
        }
        return saveOutcome + "?faces-redirect=true&" + idParameterName + "=" + stringFromId(selected.getId());
    }

    public T getSelected() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null && selected == null) {
            String idStr = fc.getExternalContext().getRequestParameterMap().get(idParameterName);
            if (!Strings.isNullOrEmpty(idStr)) {
                selected = this.convert(idStr);
            }
        }
        if (selected == null) {
            return selected = createNewInstance();
        }
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
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


    public String getEditOutcome() {
        return editOutcome;
    }

    public void setEditOutcome(String editOutcome) {
        this.editOutcome = editOutcome;
    }

    public String getListOutcome() {
        return listOutcome;
    }

    public void setListOutcome(String listOutcome) {
        this.listOutcome = listOutcome;
    }

    public String getCreateOutcome() {
        return createOutcome;
    }

    public void setCreateOutcome(String createOutcome) {
        this.createOutcome = createOutcome;
    }

    public String getIdParameterName() {
        return idParameterName;
    }

    public String getSaveOutcome() {
        return saveOutcome;
    }

    public void setSaveOutcome(String saveOutcome) {
        this.saveOutcome = saveOutcome;
    }
}
