package jsf.util3.validator;

import com.google.common.collect.Iterables;
import jsf.util3.JsfUtil;
import org.entity3.IHierarchical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.util.Properties;

/**
 * Created by home on 18.03.17.
 */
@Component
@Scope("request")
public class CircleHierarchicalValidator {
    private static String CIRCLE_ERROR_MESSAGE = "circleErrorMessage";

    @Autowired
    @Qualifier("jsf-messages")
    protected ResourceBundleMessageSource msg;

    UIInput childsInput;

    UIInput parentsInput;

    public void validate(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();

        Iterable<IHierarchical> childs = (Iterable<IHierarchical>) childsInput.getLocalValue();
        Iterable<IHierarchical> parents = (Iterable<IHierarchical>) parentsInput.getLocalValue();
        if (childs != null && parents != null &&
            Iterables.any(childs, c -> c.isParentOf(parents) || Iterables.contains(parents, c))) {
            JsfUtil.addErrorMessage(msg.getMessage(CIRCLE_ERROR_MESSAGE,null,LocaleContextHolder.getLocale()));
            fc.renderResponse();
        }
    }

    public UIInput getChildsInput() {
        return childsInput;
    }

    public void setChildsInput(UIInput childsInput) {
        this.childsInput = childsInput;
    }

    public UIInput getParentsInput() {
        return parentsInput;
    }

    public void setParentsInput(UIInput parentsInput) {
        this.parentsInput = parentsInput;
    }
}
