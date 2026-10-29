package org.example.orderService.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.orderService.DTO.Request.OrderItemReq;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    @NotEmpty
    private String orderId;

    @NotNull
    private List<OrderItemReq> deductProductItemReqList;
}
