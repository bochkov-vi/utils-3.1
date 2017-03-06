package test;

import org.entity3.IdableEntity;

import javax.persistence.Entity;

/**
 * Created by bochkov on 03.03.17.
 */
@Entity
public class Entity1 extends IdableEntity<String> {
    String id;

    public void setId(String id) {
        this.id = id;
    }

    @javax.persistence.Id
    @Override
    public String getId() {
        return null;
    }
}
