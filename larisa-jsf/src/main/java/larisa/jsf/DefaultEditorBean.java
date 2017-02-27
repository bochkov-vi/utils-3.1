package larisa.jsf;

import jsf.util3.EditManagedBean;
import larisa.EntityRepository;
import larisa.entity.AbstractEntity;
import org.entity3.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

/**
 * Created by home on 24.02.17.
 */
@Scope("view")
public class DefaultEditorBean<T extends AbstractEntity<ID>, ID extends Serializable> extends EditManagedBean<T, ID> {

    @Autowired
    EntityRepository<T, ID> repository;

    @Override
    protected CustomRepository<T, ID> getRepository() {
        return repository;
    }
}
