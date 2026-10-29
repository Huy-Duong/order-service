package org.example.orderService.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private Integer price;
    private Integer stock;
    private String categoryId;
}
