package org.entity3;

import com.google.common.base.Objects;
import org.eclipse.persistence.annotations.Customizer;
import org.entity3.column.ColumnPosition;
import org.entity3.column.EntityColumnPositionCustomizer;
import org.entity3.converter.JodaDateTimeConverter;
import org.joda.time.DateTime;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(AbstractEntity.Listener.class)
@Customizer(EntityColumnPositionCustomizer.class)
public abstract class AbstractEntity<ID extends Serializable> extends IdableEntity<ID> implements Persistable<ID>, IIdable<ID>, Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    @Convert(converter = JodaDateTimeConverter.class)
    @ColumnPosition(100)
    DateTime createdDate;


    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime сreatedDate) {
        this.createdDate = сreatedDate;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public static class Listener {
        @PrePersist
        public void prePersist(Object e) {
            ((AbstractEntity<Serializable>) e).setCreatedDate(new DateTime());
        }
    }
}
