package jsf.util3;

import com.google.common.base.Strings;
import org.entity3.IIdable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Persistable;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bochkov on 30.06.17.
 */
public abstract class JsfEditService<T extends Persistable<ID> & IIdable<ID>, ID extends Serializable> extends ManagedConverter<T, ID> {
    public static final String ERROR_ON_SAVE = "errorOnSave";
    public static final String ERROR_ON_DELETE = "errorOnDelete";
    public static final String ERROR_ON_EMPTY = "errorOnEmptySelected";
    public static final String INFO_ON_SAVE = "infoOnObjectSave";
    public static final String INFO_ON_OBJECT_EXISTS = "infoOnObjectExists";
    protected String idParameterName = "id";
    @Autowired
    @Qualifier("jsf-util-messages")
    protected Properties msg;

    public JsfEditService() {
        super();
    }

    public JsfEditService(Class<T> entityClass, Class<ID> idClass) {
        super(entityClass, idClass);
    }

    public JsfEditService(Class<T> entityClass) {
        super(entityClass);
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

    public String getIdParameterName() {
        return idParameterName;
    }
}
