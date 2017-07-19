package jsf.util3.service;

import org.entity3.IIdable;
import org.entity3.service.EntityService;
import org.springframework.data.domain.Persistable;

import javax.faces.convert.Converter;
import java.io.Serializable;

/**
 * Created by bochkov on 02.07.17.
 */
public interface JsfEntityService<T extends Persistable<ID> & IIdable<ID>, ID extends Serializable> extends Converter, org.springframework.core.convert.converter.Converter<String, T>,EntityService<T,ID> {
    String ERROR_ON_SAVE = "errorOnSave";
    String ERROR_ON_DELETE = "errorOnDelete";
    String ERROR_ON_EMPTY = "errorOnEmptySelected";
    String INFO_ON_SAVE = "infoOnObjectSave";
    String INFO_ON_OBJECT_EXISTS = "infoOnObjectExists";

    T entityFromRequest(String parameterName);

    T entityFromRequest();

    T createNewInstance();

    String getIdParameterName();

    ID idFromString(String s) throws RuntimeException;

    String stringFromId(ID id);
}
