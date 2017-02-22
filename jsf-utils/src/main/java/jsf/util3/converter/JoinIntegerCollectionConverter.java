/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.util3.converter;

import javax.faces.convert.FacesConverter;

/**
 * @author viktor
 */
@FacesConverter("integerCollectionConverter")
public class JoinIntegerCollectionConverter extends JoinCollectionConverter<Integer> {
    public JoinIntegerCollectionConverter() {
        super();
    }
}
