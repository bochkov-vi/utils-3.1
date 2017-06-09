/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf.util3;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Map;


/**
 * @author bochkov
 */
public class ViewScope implements Scope {


    public Object get(final String name, final ObjectFactory objectFactory) {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = fc.getViewRoot();
        if (viewRoot != null) {
            final Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();

            if (viewMap.containsKey(name)) {
                Object bean = viewMap.get(name);

//            // restore a transient autowired beans after re-serialization bean
//            WebApplicationContext webAppContext = ContextLoader.getCurrentWebApplicationContext();
//            AutowireCapableBeanFactory autowireFactory = webAppContext.getAutowireCapableBeanFactory();
//
//            if (webAppContext.containsBean(name)) {
//                // Reconfigure resored bean instance.
//                bean = autowireFactory.configureBean(bean, name);
//            }
//            // end restore
                return bean;
            } else {
                final Object object = objectFactory.getObject();
                viewMap.put(name, object);
                return object;
            }
        }
        return null;
    }


    public String getConversationId() {
        return null;
    }


    public void registerDestructionCallback(final String name, final Runnable callback) {
        // Not supported
    }

    public Object remove(final String name) {
        return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
    }

    public Object resolveContextualObject(final String key) {
        return null;
    }

}