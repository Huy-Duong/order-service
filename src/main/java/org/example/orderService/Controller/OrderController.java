package org.example.orderService.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.orderService.DTO.BaseResponse;
import org.example.orderService.DTO.Request.OrderCreateReq;
import org.example.orderService.DTO.Response.OrderCreateResponse;
import org.example.orderService.Entity.Orders;
import org.example.orderService.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/order/v1/")
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<Orders>> createOrder(@RequestBody @Valid OrderCreateReq orderCreateReq){
        return ResponseEntity.ok(new BaseResponse<>(orderService.createOrder(orderCreateReq),"successfully."));
    }
}
