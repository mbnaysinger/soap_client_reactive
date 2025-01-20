package com.pedidovenda.pedido.api.v1.rest;

import com.pedidovenda.pedido.api.v1.dto.order.OrderDTO;
import com.pedidovenda.pedido.api.v1.dto.order.OrderResponseDTO;
import com.pedidovenda.pedido.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/generatedSalesOrder")
    public Mono<ResponseEntity<OrderResponseDTO>> generatedSalesOrder(@RequestBody @Valid OrderDTO orderDto,
                                                                      @RequestHeader("token") String token,
                                                                      @RequestParam("origin") String origin) {

        try {
            return orderService.generOrder(orderDto, token, origin)
                    .map(orderId -> new ResponseEntity<>(orderId, HttpStatus.OK))
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
