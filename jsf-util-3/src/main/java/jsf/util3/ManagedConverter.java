package jsf.util3;

import com.google.common.base.Strings;
import org.entity3.IIdable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bochkov on 19.03.14.
 */
public abstract class ManagedConverter<T extends IIdable<ID>, ID extends Serializable> extends AbstractConverter<T, ID> {

    protected ManagedConverter() {
        super();
    }

    protected ManagedConverter(Class<T> entityClass, Class<ID> idClass) {
        super(entityClass, idClass);
    }

    protected ManagedConverter(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public T convert(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return null;
        }
        ID id = idFromString(s);
        if (id != null) {
            return getRepository().findOne(id);
        }
        return null;
    }

    public T convertAny(Object s) {
        if (s instanceof String) {
            return convert((String) s);
        } else if (s != null) {
            return convert(String.valueOf(s));
        }
        return null;
    }


    public List<T> listByIterable(Iterable<ID> ids) {
        return getRepository().findAll(ids);
    }

    protected abstract JpaRepository<T, ID> getRepository();
}
