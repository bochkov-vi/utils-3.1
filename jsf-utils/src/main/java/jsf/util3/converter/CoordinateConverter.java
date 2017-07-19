/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author bochkov
 */

public class CoordinateConverter extends com.bochkov.coordinate.CoordinateConverter implements Converter{


    public CoordinateConverter(float min, float max, String minusSuffix, String plusSuffix) {
        super(min, max, minusSuffix, plusSuffix);
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        return getAsObject(string);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        return getAsString(((Number) o).floatValue());
    }

}
