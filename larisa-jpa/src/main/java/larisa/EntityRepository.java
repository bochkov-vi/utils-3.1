package larisa;

import larisa.entity.AbstractEntity;
import org.entity3.repository.CustomRepository;

import java.io.Serializable;

/**
 * Created by home on 25.02.17.
 */

public interface EntityRepository<T extends AbstractEntity, ID extends Serializable> extends CustomRepository <T,ID>{
}
