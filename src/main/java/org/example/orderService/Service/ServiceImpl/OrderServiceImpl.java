package org.example.orderService.Service.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderService.Client.ProductClient;
import org.example.orderService.DTO.OrderCreatedEvent;
import org.example.orderService.DTO.ProductDTO;
import org.example.orderService.DTO.Request.DeductProductReq;
import org.example.orderService.DTO.Request.OrderCreateReq;
import org.example.orderService.DTO.Response.OrderCreateResponse;
import org.example.orderService.DTO.Request.OrderItemReq;
import org.example.orderService.DTO.client.Request.ProductFilter;
import org.example.orderService.Entity.OrderItem;
import org.example.orderService.Entity.Orders;
import org.example.orderService.Exception.ApplicationException;
import org.example.orderService.Repository.OrderItemRepository;
import org.example.orderService.Repository.OrderRepository;
import org.example.orderService.Service.OrderService;
import org.example.orderService.common.OrderStatus;
import org.springframework.context.ApplicationContextException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @Override
    public Orders createOrder(OrderCreateReq orderCreateReq) {

        //Lấy dữ liệu của List ProductId
        List<String> productIds = orderCreateReq.getOrderItems().stream().map(OrderItemReq :: getProductId).distinct().toList();

        //Lấy ra các bản ghi của Products
        List<ProductDTO> products = productClient.getProductsByIds(new ProductFilter(productIds));

        //Map thông tin id và productDTO
        Map<String, ProductDTO> productPriceMap = new HashMap<>();
        products.forEach(productDTO -> {
            productPriceMap.put(productDTO.getId(), productDTO);
        });


        //Tạo order
        Orders order = new Orders();
        order.setCustomerId(orderCreateReq.getCustomerId());
        order.setStatus(OrderStatus.NEW.name());
        order.setTotalAmount(0);
        Orders savedOrder = orderRepository.save(order);

        //Tính giá trị đơn
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderItemReq orderItemReq : orderCreateReq.getOrderItems()) {
            ProductDTO productDTO = productPriceMap.get(orderItemReq.getProductId());
            if(productDTO == null){
                throw new ApplicationException("Product " + orderItemReq.getProductId() + " not existed.");
            }

            if(orderItemReq.getQuantity() > productDTO.getStock()){
                throw new ApplicationException("Product " + orderItemReq.getProductId() + " not enough.");
            }

            Integer price = productDTO.getPrice();
            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setProductId(orderItemReq.getProductId());
            item.setPrice(price);
            item.setQuantity(orderItemReq.getQuantity());

            orderItemList.add(item);
            totalAmount += (price * orderItemReq.getQuantity());
        }

        orderItemRepository.saveAll(orderItemList);
        savedOrder.setTotalAmount(totalAmount);

        //Call qua productService để thực hiện lock sản phẩm
        //Trả về gì, kết quả lock sản phẩm thành công hay chưa

        //Lấy ra List Deduct Product Item
        List<OrderItemReq> orderItemReqList = orderCreateReq.getOrderItems();

//        productClient.lockProductQuantity(new DeductProductReq(orderItemReqList));



        Orders createdOrder = orderRepository.save(savedOrder);

        //Tạo event để Consumer lấy thông tin dễ dàng
        OrderCreatedEvent event = new OrderCreatedEvent(createdOrder.getId(), orderItemReqList);
        kafkaTemplate.send("order_created",event);
        log.info("Publish new order success to order_created");


        return createdOrder;

        //Validate dữ liệu đầu vào
//        List<String> orderItemIdList = orderCreateReq.getOrderItemIdList();
//        List<OrderItem> orderItemList = new ArrayList<>();
//
//        for(String id: orderItemIdList){
//            var itemId = orderItemRepository.findById(id);
//
//            if(itemId.isEmpty()){
//                throw new ApplicationContextException("Order Item not found.");
//            }
//            else {
//                OrderItem item = itemId.get();
//                orderItemList.add(item);
//            }
//        }

        //Tính giá của OrderItem

        //Thực hiện Locking sử dụng Atomic Update


        //Save new Order
//        List<OrderItemReq> orderItemResponselist = orderItemList.stream().map(orderItem -> {
//            OrderItemReq orderItemResponse = new OrderItemReq();
//            orderItemResponse.setPrice(orderItem.getPrice());
//            orderItemResponse.setQuantity(orderItem.getQuantity());
//
//            return orderItemResponse;
//        }).toList();

//        Orders newOrder = new Orders();
//        newOrder.setStatus("PENDING");
//
//        OrderCreateResponse response = new OrderCreateResponse();
//        response.setStatus(newOrder.getStatus());
//        response.setOrderItemReqList(orderItemResponselist);
//        response.setTotalAmount(newOrder.getTotalAmount());
//
//
//        return response;
    }

    @Override
    public void statusChanging(String orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new ApplicationException("No Order found."));

        orders.setStatus("PREPARED");

        orderRepository.save(orders);
    }
}
