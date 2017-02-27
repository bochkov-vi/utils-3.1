package web;

import com.sun.faces.application.resource.ResourceHandlerImpl;
import larisa.repository.FileRepository;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by home on 27.02.17.
 */
public class FileResourceHandler extends ResourceHandlerImpl {
    private static Logger log = Logger.getLogger(FileResourceHandler.class.getName());

    String libraryName = "resources/fileRepository";

    FileRepository fileRepository;

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        log.log(Level.FINE, "Delegating resource creation to default implementation. name: " + resourceName +
                " and library: " + libraryName);
        if (this.libraryName.equals(libraryName)) {
            return new FileResource(resourceName, libraryName, getRepository().findOne(new Integer(resourceName)));
        }
        return super.createResource(resourceName, libraryName, null);
    }

    @Override
    public boolean libraryExists(String libraryName) {
        if (this.libraryName.equals(libraryName)) {
            return true;
        }
        return super.libraryExists(libraryName);
    }

    FileRepository getRepository() {
        if (fileRepository == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            return fileRepository = (FileRepository) context.getApplication().getELResolver().getValue(context.getELContext(), null, libraryName);
        } else {
            return fileRepository;
        }
    }
}
