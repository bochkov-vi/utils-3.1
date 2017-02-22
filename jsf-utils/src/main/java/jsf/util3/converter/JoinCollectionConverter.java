/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.converter;

import com.google.common.base.Joiner;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 * @author viktor
 */
public class JoinCollectionConverter<T> implements Converter {

    Class elementClass;

    public JoinCollectionConverter() {
        try {
            elementClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Collection) {
            return Joiner.on("; ").join((Collection) value);
        } else {
            return "";
        }
    }

}
