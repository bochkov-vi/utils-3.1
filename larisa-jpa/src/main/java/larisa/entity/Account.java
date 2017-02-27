package larisa.entity;

import org.entity3.converter.JodaLocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by home on 23.02.17.
 */
@Entity
@Table(name = "account")
public class Account extends AbstractEntity<String> {
    @Id
    @Column(name = "login")
    String id;

    @Column(name = "password")
    String password;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.DATE)
    @Convert(converter = JodaLocalDateConverter.class)
    LocalDate expirationDate;

    @Enumerated
    @Column(name = "role")
    @ElementCollection
    @CollectionTable(name = "account_role")
    List<Role> roles;

    @OneToOne
    @JoinColumn(name = "id_customer")
    Customer customer;

    public Account() {
    }

    public Account(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
