package jsf.util3.validator;

import org.entity3.IHierarchical;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Created by home on 18.03.17.
 */

public abstract class CircleHierarchicalValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        IHierarchical entity = (IHierarchical) component.getAttributes().get("entity");
        if (value instanceof Iterable) {
            for (IHierarchical h : (Iterable<? extends IHierarchical>) value) {
                validateCircle( entity,h);
            }
        } else {
            validateCircle(entity, (IHierarchical) value);
        }
    }

    protected abstract void validateCircle(IHierarchical validated, IHierarchical value) throws ValidatorException;

}
