package com.pedidovenda.pedido.domain.service;

import com.pedidovenda.pedido.api.v1.dto.order.OrderDTO;
import com.pedidovenda.pedido.api.v1.dto.order.OrderResponseDTO;
import com.pedidovenda.pedido.config.OrderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrderClient orderClient;

    @Autowired
    public OrderService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    public Mono<OrderResponseDTO> generOrder(OrderDTO orderDto, String token, String origin) throws Exception {
        return orderClient.callSoapClient(orderDto, token, origin);
    }
}