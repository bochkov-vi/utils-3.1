package larisa.entity;

import org.entity3.IGetNamed;
import org.entity3.IHierarchical;
import org.entity3.column.ColumnPosition;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bochkov
 */
@Entity
@Table(name = "product_type")
public class ProductType extends AbstractAuditableEntity<Integer> implements IGetNamed, IHierarchical<Integer, ProductType>,IGetFiles {
    @Id
    @GeneratedValue
    @Column(name = "id_product_type")
    @ColumnPosition(1)
    Integer id;

    @Column(name = "name", unique = true)
    @ColumnPosition(2)
    String name;

    @Column(name = "volume_note", length = 45)
    @ColumnPosition(3)
    String volumeNote;


    @ManyToMany
    @JoinTable(name = "product_type_file", joinColumns = @JoinColumn(name = "id_product_type"), inverseJoinColumns = @JoinColumn(name = "id_file"))
    List<File> files;

    @ManyToMany
    @JoinTable(name = "product_type_p", joinColumns = @JoinColumn(name = "id_product_type",referencedColumnName = "id_product_type"), inverseJoinColumns = @JoinColumn(name = "id_product_type_parent",referencedColumnName = "id_product_type"))
    List<ProductType> childs;

    @ManyToMany
    @JoinTable(name = "product_type_p", joinColumns = @JoinColumn(name = "id_product_type_parent",referencedColumnName = "id_product_type"), inverseJoinColumns = @JoinColumn(name = "id_product_type",referencedColumnName = "id_product_type"))
    List<ProductType> parents;


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolumeNote() {
        return volumeNote;
    }

    public void setVolumeNote(String volumeNote) {
        this.volumeNote = volumeNote;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public List<ProductType> getChilds() {
        return childs;
    }

    public void setChilds(List<ProductType> childs) {
        this.childs = childs;
    }

    @Override
    public List<ProductType> getParents() {
        return parents;
    }

    public void setParents(List<ProductType> parents) {
        this.parents = parents;
    }
}
