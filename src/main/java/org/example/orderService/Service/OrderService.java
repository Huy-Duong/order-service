package org.example.orderService.Service;

import org.example.orderService.DTO.Request.OrderCreateReq;
import org.example.orderService.DTO.Response.OrderCreateResponse;
import org.example.orderService.Entity.Orders;

public interface OrderService {

    Orders createOrder(OrderCreateReq orderCreateReq);

    void statusChanging(String orderId);
}
