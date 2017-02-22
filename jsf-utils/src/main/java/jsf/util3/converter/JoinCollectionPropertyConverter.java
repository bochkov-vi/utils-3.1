package jsf.util3.converter;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Collection;

/**
 * Created by bochkov on 31.01.17.
 */
@FacesConverter("joinCollectionPropertyConverter")
public class JoinCollectionPropertyConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent component, Object value) {
        if (value instanceof Collection) {
            String expression = (String) component.getAttributes().get("property");
            Collection c = (Collection) value;
            ELContext ec = fc.getELContext();
            return Joiner.on("; ").join(
                    Sets.newHashSet(Collections2.filter(
                            Collections2.transform(c, obj -> ec.getELResolver().getValue(ec, obj, expression)), Predicates.notNull()
                    ))
            );
        } else {
            return "";
        }
    }

}
