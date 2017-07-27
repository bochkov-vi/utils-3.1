package jsf.util3.service.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import jsf.util3.EditManagedBean;
import jsf.util3.JsfUtil;
import jsf.util3.service.JsfEntityService;
import org.entity3.IIdable;
import org.entity3.service.impl.EntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Persistable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jsf.util3.AbstractConverter.makeIdFromString;
import static jsf.util3.AbstractConverter.makeStringFromId;
import static jsf.util3.JsfUtil.addErrorMessage;
import static jsf.util3.JsfUtil.addInfoMessage;

/**
 * Created by bochkov on 13.06.17.
 */
@Configurable
public abstract class AbstractJsfEntityService<T extends Persistable<ID> & IIdable<ID>, ID extends Serializable>
        extends EntityServiceImpl<T, ID> implements JsfEntityService<T, ID> {
    @Autowired
    @Qualifier("jsf-util-messages")
    transient protected Properties msg;

    protected String idParameterName = "id";
    protected Class<ID> idClass;


    public AbstractJsfEntityService(Class<T> entityClass, Class<ID> idClass) {
        super(entityClass);
        this.idClass = idClass;
    }

    public AbstractJsfEntityService() {
        super();
        idClass = ((Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    public AbstractJsfEntityService(Class<T> entityClass) {
        super(entityClass);
    }


    @Override
    public void delete(T selected) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (selected != null) {
            try {
                getRepository().delete(selected);
                selected = null;
            } catch (Exception e) {
                JsfUtil.addErrorMessage(msg.getProperty(ERROR_ON_DELETE), ((NestedRuntimeException) e).getRootCause());
            }
        } else {
            addErrorMessage(msg.getProperty(ERROR_ON_EMPTY));
        }
    }

    @Override
    public <S extends T> S save(S selected) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            selected = getRepository().saveAndFlush(selected);
            addInfoMessage(MessageFormat.format("{0}{1}", msg.getProperty(INFO_ON_SAVE), selected.getId()));
        } catch (NestedRuntimeException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(msg.getProperty(ERROR_ON_SAVE), e.getRootCause());
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage(msg.getProperty(ERROR_ON_SAVE), MoreObjects.firstNonNull(e.getCause(), e));
        }
        return selected;
    }


    @Override
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

    @Override
    public T entityFromRequest() {
        return entityFromRequest(idParameterName);
    }


    @Override
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

    @Override
    public String getIdParameterName() {
        return idParameterName;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return convert(value);
    }


    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o != null) {
            return stringFromId(((Persistable<ID>) o).getId());
        }
        return "";
    }

    @Override
    public T convert(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return null;
        }
        ID id = idFromString(s);
        if (id != null) {
            return getRepository().findOne(id);
        }
        return null;
    }

    @Override
    public ID idFromString(String s) throws RuntimeException {
        return makeIdFromString(idClass, s);
    }

    @Override
    public String stringFromId(ID id) {
        return makeStringFromId(id);
    }

}