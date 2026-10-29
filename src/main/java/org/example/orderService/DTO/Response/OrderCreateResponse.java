package org.example.orderService.DTO.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.orderService.DTO.Request.OrderItemReq;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateResponse {

    private String status;

    private Integer totalAmount;

    private List<OrderItemReq> orderItemReqList;

}
