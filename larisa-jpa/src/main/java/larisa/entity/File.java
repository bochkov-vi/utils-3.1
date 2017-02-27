package larisa.entity;

import org.entity3.column.ColumnPosition;

import javax.persistence.*;

/**
 * Created by home on 23.02.17.
 */
@Entity
@Table(name = "file")
public class File extends AbstractAuditableEntity<Integer> {
    @Id
    @Column(name = "id_file")
    @ColumnPosition(1)
    @GeneratedValue(generator = "file")
    @TableGenerator(name = "file", initialValue = 1, allocationSize = 1)
    Integer id;

    @Column(name = "name")
    @ColumnPosition(1)
    String name;

    @ColumnPosition(2)
    @Column(name = "type")
    String type;

    @ColumnPosition(3)
    @Column(name = "encoding")
    String encoding;

    @Lob
    @ColumnPosition(4)
    @Column(name = "data", nullable = false)
    byte[] data;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
