/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bochkov.coordinate;

/**
 * @author bochkov
 */
public class LatitudeConverter extends CoordinateConverter {
    static String S = "ЮШ";
    static String N = "СШ";

    public LatitudeConverter() {
        super(-90, 90, S, N);
    }
}
