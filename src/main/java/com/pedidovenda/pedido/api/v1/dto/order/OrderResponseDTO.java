package com.pedidovenda.pedido.api.v1.dto.order;

import java.time.LocalDateTime;

public class OrderResponseDTO {

    private String application;
    private String salesOrder;
    private LocalDateTime createdAt;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(String salesOrder) {
        this.salesOrder = salesOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderResponseDTO{" +
                "application='" + application + '\'' +
                ", salesOrder='" + salesOrder + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
