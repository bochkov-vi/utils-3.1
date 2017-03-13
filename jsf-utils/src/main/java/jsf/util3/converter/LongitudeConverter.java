/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.converter;

import javax.faces.convert.FacesConverter;

/**
 * @author bochkov
 */
@FacesConverter("longitudeConverter")
public class LongitudeConverter extends CoordinateConverter {
    static String W = "ЗД";
    static String E = "ВД";

    public LongitudeConverter() {
        super(-180, 180, W, E);
    }
}
