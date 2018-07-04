/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bochkov.coordinate;

import java.io.Serializable;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;

/**
 * @author bochkov
 */
public class CoordinateConverter implements Serializable {

    public static char GRADCHAR = '\u00B0';

    float min;

    float max;

    ChoiceFormat suffixForm;

    MessageFormat form;

    String minusSuffix;

    String plusSuffix;


    public CoordinateConverter(float min, float max, String minusSuffix, String plusSuffix) {
        this.min = min;
        this.max = max;
        this.minusSuffix = minusSuffix;
        this.plusSuffix = plusSuffix;
        suffixForm = new ChoiceFormat(new double[]{-1f, 1f}, new String[]{minusSuffix, plusSuffix});
        form = new MessageFormat("{0,number,#}" + GRADCHAR + "{1,number,#.#}''{2}");
        form.setFormat(2, suffixForm);
    }

    public float[] toGradMin(Float v) {
        if (v != null) {
            int grad = Math.abs(v.intValue());
            if (v < min) {
                throw new RuntimeException(MessageFormat.format("Значение {0} меньше допустимого {1}", v, min));
            }
            if (v > max) {
                throw new RuntimeException(MessageFormat.format("Значение {0} больше допустимого {1}", v, max));
            }
            float mn = 60.0f * (Math.abs(v.floatValue()) - Math.abs(v.intValue()));
            return new float[]{grad, mn};
        }
        return null;
    }


    public Float getAsFloat(String string) {
        return Optional.ofNullable(getAsObject(string)).map(Number::floatValue).orElse(null);
    }

    public Double getAsDouble(String string) {
        return Optional.ofNullable(getAsObject(string)).map(Number::doubleValue).orElse(null);
    }

    public Number getAsObject(String string) {
        if (string != null && !string.trim().isEmpty()) {
            String[] data = string.split("[^0-9,.]+");
            if (data.length > 0) {
                double v = 0f;
                for (int i = 0; i < data.length; i++) {
                    if (data[i].matches("[\\d,.]+")) {
                        try {
                            double d = NumberFormat.getNumberInstance().parse(data[i]).doubleValue();
                            v = v + d * 1 / Math.pow(60, i);
                        } catch (ParseException parseException) {
                        }
                    }
                }
                if (string.toUpperCase().contains(minusSuffix)) {
                    v = v * -1;
                }
                return (float) v;
            }
        }
        return null;
    }

    public String getAsString(Number o) {
        if (o != null) {
            Float v = (Float) o.floatValue();
            float[] gm = toGradMin(v);
            if (gm != null) {
                return form.format(new Object[]{gm[0], gm[1], Math.signum(v)});
            }
        }
        return "";
    }
}
