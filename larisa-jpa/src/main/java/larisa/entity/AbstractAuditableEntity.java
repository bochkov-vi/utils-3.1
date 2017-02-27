package larisa.entity;


import org.entity3.column.ColumnPosition;
import org.entity3.converter.JodaDateTimeConverter;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bochkov
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity<ID extends Serializable> extends AbstractEntity<ID> implements Auditable<Account, ID> {

    @ManyToOne
    @JoinColumn(name = "editor", nullable = false)
    @ColumnPosition(98)
    Account lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    @ColumnPosition(99)
    Account createdBy;

    @ColumnPosition(100)
    @Column(name = "edited_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = JodaDateTimeConverter.class)
    DateTime lastModifiedDate;


    @Override
    public Account getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(Account lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Account getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
