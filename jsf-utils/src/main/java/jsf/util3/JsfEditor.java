package jsf.util3;

import org.entity3.IIdable;
import org.springframework.data.domain.Persistable;

import javax.faces.convert.Converter;
import java.io.Serializable;

/**
 * Created by bochkov on 02.07.17.
 */
public interface JsfEditor<T extends Persistable<ID> & IIdable<ID>, ID extends Serializable> extends Converter{
    String ERROR_ON_SAVE = "errorOnSave";
    String ERROR_ON_DELETE = "errorOnDelete";
    String ERROR_ON_EMPTY = "errorOnEmptySelected";
    String INFO_ON_SAVE = "infoOnObjectSave";
    String INFO_ON_OBJECT_EXISTS = "infoOnObjectExists";

    void delete(T selected);

    T save(T selected);

    T entityFromRequest(String parameterName);

    T entityFromRequest();

    T createNewInstance();

    String getIdParameterName();
}
