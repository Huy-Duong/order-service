package org.example.orderService.DTO.client.Request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductFilter {
    private List<String> ids;
}
