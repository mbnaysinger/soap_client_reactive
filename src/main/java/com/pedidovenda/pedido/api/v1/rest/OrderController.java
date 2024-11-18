package com.pedidovenda.pedido.api.v1.rest;

import com.pedidovenda.pedido.api.v1.dto.order.OrderDTO;
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

    @PostMapping("/gerar")
    public Mono<ResponseEntity<Long>> gerarPedido(@RequestBody @Valid OrderDTO orderDto,
                                                  @RequestHeader("Authorization") String token) {

        System.out.println("Recebendo pedido com OrderDTO: " + orderDto);
        System.out.println("Token de autorização: " + token);
        try {
            return orderService.gerarPedido(orderDto, token)
                    .map(orderId -> new ResponseEntity<>(orderId, HttpStatus.OK))
                    .onErrorResume(e -> {
                        // Log the error and handle it gracefully
                        e.printStackTrace();
                        return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                    });
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
