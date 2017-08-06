package jsf.util3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

import javax.el.ELContext;
import javax.el.ELException;
import javax.faces.context.FacesContext;
import java.util.Locale;


public class SpringMessageSourceElResolver extends SpringBeanFacesELResolver {

    private final static Log log = LogFactory.getLog(SpringMessageSourceElResolver.class);

    @Override
    public Object getValue(ELContext elContext, Object base, Object property) throws ELException {
        if (log.isDebugEnabled())
            log.debug("getValue(" + elContext + ", " + base + ", " + property + ")");

        if (base instanceof MessageSource && property instanceof String) {
            if (log.isDebugEnabled()) {
                log.debug("getting message from MessageSource with key: " + property + " for locale: " + getLocale());
            }

            String result = ((MessageSource) base).getMessage((String) property, null, getLocale());

            if (log.isDebugEnabled()) {
                log.debug("Result: " + result);
            }

            if (null != result) {
                elContext.setPropertyResolved(true);
            }

            return result;
        }

        return super.getValue(elContext, base, property);
    }

    private Locale getLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestLocale();

    }
}
