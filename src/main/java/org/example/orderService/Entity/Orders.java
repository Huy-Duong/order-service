package org.example.orderService.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id;

    @Column(name = "customer_id")
    private String customerId;

    private String status;

    @Column(name = "total_amount")
    private Integer totalAmount;
}
