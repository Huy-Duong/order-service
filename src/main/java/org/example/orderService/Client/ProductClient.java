package org.example.orderService.Client;

import org.example.orderService.DTO.ProductDTO;
import org.example.orderService.DTO.Request.DeductProductReq;
import org.example.orderService.DTO.client.Request.ProductFilter;

import java.util.List;

public interface ProductClient {

    List<ProductDTO> getProductsByIds(ProductFilter productFilter);

    void lockProductQuantity(DeductProductReq deductProductReq);
}
