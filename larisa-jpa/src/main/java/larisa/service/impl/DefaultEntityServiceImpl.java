package larisa.service.impl;

import larisa.entity.AbstractEntity;
import larisa.EntityRepository;
import org.entity3.repository.CustomRepository;
import org.entity3.service.impl.EntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by home on 25.02.17.
 */
@NoRepositoryBean
public class DefaultEntityServiceImpl<T extends AbstractEntity<ID>, ID extends Serializable> extends EntityServiceImpl<T, ID> {
    @Autowired
    EntityRepository<T, ID> repository;

    @Override
    public CustomRepository<T, ID> getRepository() {
        return repository;
    }

    public DefaultEntityServiceImpl() {
    }

    public DefaultEntityServiceImpl(Class<T> entityClass) {
        super(entityClass);
    }

    public DefaultEntityServiceImpl(String... maskedProperty) {
        super(maskedProperty);
    }

    public DefaultEntityServiceImpl(Class<T> entityClass, String... maskedProperty) {
        super(entityClass, maskedProperty);
    }

    public DefaultEntityServiceImpl(Class<T> entityClass, List<String> maskedPopertyList) {
        super(entityClass, maskedPopertyList);
    }
}
