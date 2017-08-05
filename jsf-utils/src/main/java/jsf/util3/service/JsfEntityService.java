package jsf.util3.service;

import org.entity3.service.EntityService;
import org.springframework.data.domain.Persistable;

import javax.faces.convert.Converter;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by bochkov on 02.07.17.
 */
public interface JsfEntityService<T extends Persistable<ID> , ID extends Serializable> extends Converter, org.springframework.core.convert.converter.Converter<String, T>,EntityService<T,ID> {
    public static  String ERROR_ON_SAVE = "errorOnSave";
    public static String ERROR_ON_DELETE = "errorOnDelete";
    public static String ERROR_ON_EMPTY = "errorOnEmptySelected";
    public static  String INFO_ON_SAVE = "infoOnObjectSave";
    public static  String INFO_ON_OBJECT_EXISTS = "infoOnObjectExists";

    T entityFromRequest(String parameterName);

    T entityFromRequest();

    T createNewInstance();

    String getIdParameterName();

    ID idFromString(String s) throws RuntimeException;

    String stringFromId(ID id);

    T clone(@NotNull T original);
}
