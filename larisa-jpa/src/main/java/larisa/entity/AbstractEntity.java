package larisa.entity;


import org.eclipse.persistence.annotations.Customizer;
import org.entity3.IIdable;
import org.entity3.column.ColumnPosition;
import org.entity3.column.EntityColumnPositionCustomizer;
import org.entity3.converter.JodaDateTimeConverter;
import org.joda.time.DateTime;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bochkov
 */

@MappedSuperclass
@EntityListeners(AbstractEntity.CreateListener.class)
@Customizer(EntityColumnPositionCustomizer.class)
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>,IIdable<ID> {
    @ColumnPosition(100)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = JodaDateTimeConverter.class)
    private DateTime createdDate;

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    public static class CreateListener {
        @PrePersist
        public void prePersist(AbstractEntity entity) {
            entity.createdDate = new DateTime();
        }

    }
}
