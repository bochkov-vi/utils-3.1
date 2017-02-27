package larisa.entity;

import javax.persistence.*;

/**
 * Created by home on 23.02.17.
 */
@Entity
@Table(name = "customer_order")
public class CustomerOrder extends AbstractEntity<Integer> {
    @Id
    @Column(name = "id_customer_order")
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @Override
    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
