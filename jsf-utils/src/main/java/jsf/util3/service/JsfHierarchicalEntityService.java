package jsf.util3.service;

import org.entity3.IHierarchical;
import org.entity3.IIdable;
import org.entity3.service.HierarchicalEntityService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * Created by bochkov on 02.07.17.
 */
public interface JsfHierarchicalEntityService<T extends IIdable<ID> & IHierarchical<ID,T>, ID extends Serializable> extends JsfEntityService<T,ID>,HierarchicalEntityService<T,ID> {
    public static String CIRCLE_ERROR_MESSAGE = "circleErrorMessage";
    boolean validateCircleLink(Iterable<IHierarchical> childs, Iterable<IHierarchical> parents);
}
