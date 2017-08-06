package jsf.util3.service.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import jsf.util3.EditManagedBean;
import jsf.util3.JsfUtil;
import jsf.util3.service.JsfEntityService;
import org.entity3.service.impl.EntityServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Persistable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.MessageFormat;
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
public abstract class JsfEntityServiceImpl<T extends Persistable<ID>, ID extends Serializable>
        extends EntityServiceImpl<T, ID> implements JsfEntityService<T, ID> {

    @Autowired
    @Qualifier("jsf-messages")
    protected transient MessageSource messageSource;

    protected String idParameterName = "id";

    protected Class<ID> idClass;


    public JsfEntityServiceImpl() {
        super();
        idClass=argument(this,1);
    }

    public JsfEntityServiceImpl(String... maskedProperty) {
        super(maskedProperty);
        idClass=argument(this,1);
    }

    public JsfEntityServiceImpl(String idParameterName, Iterable<String> maskedProperty) {
        super(maskedProperty);
        this.idParameterName = idParameterName;
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
                JsfUtil.addErrorMessage(messageSource.getMessage(ERROR_ON_DELETE,null, LocaleContextHolder.getLocale()), ((NestedRuntimeException) e).getRootCause());
            }
        } else {
            addErrorMessage(messageSource.getMessage(ERROR_ON_EMPTY,null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public <S extends T> S save(S selected) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            selected = getRepository().saveAndFlush(selected);
            addInfoMessage(MessageFormat.format("{0}{1}", messageSource.getMessage(INFO_ON_SAVE,null, LocaleContextHolder.getLocale()), selected.getId()));
        } catch (NestedRuntimeException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, messageSource.getMessage(ERROR_ON_SAVE,null, LocaleContextHolder.getLocale()), e);
            addErrorMessage( messageSource.getMessage(ERROR_ON_SAVE,null, LocaleContextHolder.getLocale()), e.getRootCause());
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_ON_SAVE, e);
            addErrorMessage( messageSource.getMessage(ERROR_ON_SAVE,null, LocaleContextHolder.getLocale()), MoreObjects.firstNonNull(e.getCause(), e));
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

    @Override
    public T clone(@NotNull T original) {
        T copy = createNewInstance();
        BeanUtils.copyProperties(original, copy, "id", "createdDate");
        return copy;
    }

}
