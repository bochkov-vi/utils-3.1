package jsf.util3;

import jsf.util3.service.JsfEntityService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Map;

public interface IEditManagedBean<T extends Persistable<ID>, ID extends Serializable>  extends JsfEntityService<T,ID>{
    String prepareCreate();

    String prepareEdit(T entity);

    String prepareDelete(T entity);

    String delete();

    String save();

    T getSelected();

    void setSelected(T selected);

    String getToListOutcome();

    String getAfterDeleteOutcome();

    String getToEditOutcome();

    String getAfterEditOutcome();

    String getAfterCreateOutcome();

    String getToCreateOutcome();

    String getToListOutcome(Map<String, String> params);

    String getAfterDeleteOutcome(Map<String, String> params);

    String getToEditOutcome(Map<String, String> params);

    String getAfterEditOutcome(Map<String, String> params);

    String getAfterCreateOutcome(Map<String, String> params);

    String getToCreateOutcome(Map<String, String> params);
}
