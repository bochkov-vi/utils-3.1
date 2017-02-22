package jsf.util3;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Created by bochkov on 29.08.2014.
 */
public abstract class JsfUtil {


    public static void addErrorMessage(String msg) {
        addErrorMessage(msg, (String) null);
    }

    public static void addErrorMessage(String msg, Exception e) {
        addErrorMessage(msg, e.getLocalizedMessage());
    }

    public static void addErrorMessage(String msg, String details) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, details));
    }

    public static void addInfoMessage(String msg, String details) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, details));
    }

    public static void addInfoMessage(String msg) {
        addInfoMessage(msg, null);
    }

    public static void addWarnMessage(String msg, String details) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, details));
    }

    public static void addWarnMessage(String msg) {
        addWarnMessage(msg, null);
    }

    public static FacesMessage createErrorMessage(String s) {
        return  new FacesMessage(FacesMessage.SEVERITY_ERROR, s,null);
    }
}
