package com.bochkov.coordinate;

/**
 * Created by bochkov on 12.07.17.
 */
public class FormatedLatitude extends LatitudeConverter {
    Number coordinate;

    public FormatedLatitude(Number coordinate) {
        this.coordinate = coordinate;
    }

    public FormatedLatitude() {
    }

    @Override
    public String toString() {
        if (coordinate != null) {
            return super.getAsString(coordinate.floatValue());
        }
        return "";
    }
}
