package web;

import com.google.common.collect.ImmutableMap;
import larisa.entity.File;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by home on 27.02.17.
 */
public class FileResource extends Resource {


    public static final String LAST_MODIFIED_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";

    File file;

    public FileResource(String resourceName, String libraryName, File file) {
        super();
        setResourceName(resourceName);
        setLibraryName(libraryName);
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(file.getData());
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return ImmutableMap.of("Last-Modified", file.getLastModifiedDate().toString(LAST_MODIFIED_PATTERN));
    }

    @Override
    public String getContentType() {
        return super.getContentType();
    }
    @Override
    public String getRequestPath() {
        StringBuilder buf = new StringBuilder(
                FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
        buf.append(ResourceHandler.RESOURCE_IDENTIFIER);
        buf.append("/").append(getLibraryName()).append(".xhtml?ln=").append(getResourceName());
        return buf.toString();
    }

    @Override
    public URL getURL() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        StringBuilder buf = new StringBuilder(context.getRequestScheme());
        buf.append(context.getRequestServerName());
        if (context.getRequestServerPort() != 80 && context.getRequestServerPort() != 443) {
            buf.append(":").append(context.getRequestServerPort());
        }
        buf.append(getRequestPath());
        URL url = null;
        try {
            url = new URL(buf.toString());

        } catch (java.net.MalformedURLException e) {
            System.err.println(e.getStackTrace());
        }
        return url;
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return true;
    }
}