package com.ss.order_service.controller;


import com.ss.order_service.dto.OrderResponseDto;
import com.ss.order_service.dto.ProductResponseDto;
import com.ss.order_service.entity.Order;
import com.ss.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

        @PostMapping("/placeOrder")
        public Mono<ResponseEntity<OrderResponseDto>> placeOrder(@RequestBody Order order) {


            return webClientBuilder.build().get().uri("http://PRODUCT-SERVICE/products" + order.getProductId()).retrieve().bodyToMono(ProductResponseDto.class).map(
                    productResponseDto -> {
                OrderResponseDto responseDto = new OrderResponseDto();

                responseDto.setProductId(order.getProductId());
                responseDto.setQuantity(order.getQuantity());
                responseDto.setProductName(productResponseDto.getName());
                responseDto.setProductPrice(productResponseDto.getPrice());

                double totalPrice = order.getQuantity() * productResponseDto.getPrice();
                responseDto.setTotalPrice(totalPrice);
                order.setPrice(totalPrice);
                order.setName(productResponseDto.getName());
                orderRepository.save(order);

                responseDto.setOrderId(order.getId());
                return ResponseEntity.ok(responseDto);
            });

        }

    @GetMapping
    public List<Order> getAllOrders (){
        return orderRepository.findAll();
    }



}
