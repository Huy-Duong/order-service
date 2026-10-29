package org.example.orderService.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@Table(name = "order_item")
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    private Integer price;

    private Integer quantity;
}
