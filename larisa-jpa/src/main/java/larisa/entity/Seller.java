package larisa.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by home on 23.02.17.
 */
@Entity
@Table(name = "seller")
public class Seller extends AbstractEntity<Integer> implements IGetFiles {
    @Id
    @Column(name = "id_seller")
    @GeneratedValue
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "note")
    String note;

    @ManyToMany
    @JoinTable(name = "seller_file", joinColumns = @JoinColumn(name = "id_seller"), inverseJoinColumns = @JoinColumn(name = "id_file"))
    List<File> files;

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
