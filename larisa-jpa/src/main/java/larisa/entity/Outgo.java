package larisa.entity;

import org.entity3.column.ColumnPosition;
import org.entity3.converter.JodaLocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by home on 23.02.17.
 */
@Entity
@Table(name = "outgo")
public class Outgo extends AbstractAuditableEntity<Integer> {
    @Id
    @GeneratedValue
    @Column(name = "id_outgo")
    Integer id;

    @Temporal(TemporalType.DATE)
    @Convert(converter = JodaLocalDateConverter.class)
    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(name = "volume", precision = 4, scale = 2)
    @ColumnPosition(4)
    Double volume;

    @ManyToOne
    @JoinColumn(name = "id_product_type")
    @ColumnPosition(3)
    ProductType productType;

    @Override
    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
