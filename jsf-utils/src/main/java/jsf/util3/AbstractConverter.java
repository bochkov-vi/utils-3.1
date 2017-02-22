package jsf.util3;

import com.google.common.base.Strings;
import org.springframework.data.domain.Persistable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by bochkov on 19.03.14.
 */
public abstract class AbstractConverter<T, ID extends Serializable> implements Serializable, Converter, org.springframework.core.convert.converter.Converter<String, T> {
    public static final char SEPARATOR = '~';

    protected Class<T> entityClass;

    protected Class<ID> idClass;

    protected AbstractConverter() {
        entityClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        idClass = ((Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    protected AbstractConverter(Class<T> entityClass, Class<ID> idClass) {
        this.entityClass = entityClass;
        if (idClass == null)
            idClass = ((Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        else
            this.idClass = idClass;
    }

    protected AbstractConverter(Class<T> entityClass) {
        this(entityClass, null);
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return convert(s);
    }

    @Override
    abstract public T convert(String s);

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o != null) {
            return stringFromId(((Persistable<ID>) o).getId());
        }
        return "";
    }

    public ID idFromString(String s) throws RuntimeException {
        ID id = null;
        if (!Strings.isNullOrEmpty(s))
            try {
                id = idClass.getConstructor(String.class).newInstance(s);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        return id;
    }

    public String stringFromId(ID id) {
        if (id != null)
            return String.valueOf(id);
        else return "";
    }

}
