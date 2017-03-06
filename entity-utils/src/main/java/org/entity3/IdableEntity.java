package org.entity3;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by bochkov on 03.03.17.
 */
public abstract class IdableEntity<ID extends Serializable> implements IIdable<ID> {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdableEntity)) {
            return false;
        }
        IdableEntity<?> that = (IdableEntity<?>) o;
        return Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
