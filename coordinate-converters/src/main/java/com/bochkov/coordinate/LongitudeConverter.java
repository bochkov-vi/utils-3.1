/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bochkov.coordinate;


/**
 * @author bochkov
 */
public class LongitudeConverter extends CoordinateConverter {
    static String W = "ЗД";
    static String E = "ВД";

    public LongitudeConverter() {
        super(-180, 180, W, E);
    }
}
