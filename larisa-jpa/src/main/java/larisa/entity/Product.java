package larisa.entity;

import org.entity3.column.ColumnPosition;
import org.entity3.converter.JodaLocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by home on 23.02.17.
 */
@Entity
@Table(name = "product")
public class Product extends AbstractAuditableEntity<Integer> implements IGetFiles {
    @Id
    @GeneratedValue
    @Column(name = "id_product")
    @ColumnPosition(1)
    Integer id;

    @Column(name = "date", nullable = true)
    @Temporal(TemporalType.DATE)
    @Convert(converter = JodaLocalDateConverter.class)
    @ColumnPosition(2)
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_product_type")
    @ColumnPosition(3)
    ProductType productType;

    @Column(name = "volume", precision = 4, scale = 2)
    @ColumnPosition(4)
    Double volume;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.DATE)
    @Convert(converter = JodaLocalDateConverter.class)
    @ColumnPosition(5)
    LocalDate expirationDate;


    @ManyToMany
    @JoinTable(name = "product_file", joinColumns = @JoinColumn(name = "id_product"), inverseJoinColumns = @JoinColumn(name = "id_file"))
    List<File> files;

    @Override
    public Integer getId() {
        return id;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public List<File> getFiles() {
        return files;
    }

    @Override
    public void setFiles(List<File> files) {
        this.files = files;
    }
}
