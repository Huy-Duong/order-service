package org.example.orderService.Client.impl;

import lombok.RequiredArgsConstructor;
import org.example.orderService.Client.ProductClient;
import org.example.orderService.DTO.BaseResponse;
import org.example.orderService.DTO.ProductDTO;
import org.example.orderService.DTO.Request.DeductProductReq;
import org.example.orderService.DTO.client.Request.ProductFilter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final WebClient.Builder webClientBuilder;

    @Override
    public List<ProductDTO> getProductsByIds(ProductFilter productFilter) {
        WebClient.Builder builder = WebClient.builder();

        BaseResponse<List<ProductDTO>> response = builder.build()
                .post()
                .uri("http://localhost:8082/product/v1/search")
                .bodyValue(productFilter)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponse<List<ProductDTO>>>() {
                }).block();
        if(response == null || response.getData() == null){
            throw new ArithmeticException("Không thể lấy thông tin sản phẩm từ Product Service");
        }

        return response.getData();
    }


    @Override
    public void lockProductQuantity(DeductProductReq deductProductReq) {
        WebClient.Builder builder = WebClient.builder();
        
        builder.build().put().uri("http://localhost:8082/product/v1/lock").bodyValue(deductProductReq).retrieve().toBodilessEntity().block();

    }
}
