/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.converter;

import javax.faces.convert.FacesConverter;

/**
 * @author bochkov
 */
@FacesConverter("latitudeConverter")
public class LatitudeConverter extends CoordinateConverter {
    static String S = "ЮШ";
    static String N = "СШ";

    public LatitudeConverter() {
        super(-90, 90, S, N);
    }
}
