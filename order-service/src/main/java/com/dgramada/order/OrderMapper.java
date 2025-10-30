package com.dgramada.order;

import com.dgramada.customer.Customer;
import com.dgramada.order.dto.OrderResponse;
import com.dgramada.order.model.Order;
import com.dgramada.order.model.OrderStatus;
import com.dgramada.product.Product;
import com.dgramada.product.dto.ProductResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class OrderMapper {

    public Order from(Customer customer, List<Product> productList) {
        final var order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(customer);
        order.setProducts(Set.copyOf(productList));
        return order;
    }

    public OrderResponse toResponse(Order order, Customer customer, List<ProductResponse> products) {
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt(),
                customer.getName(),
                customer.getEmail(),
                products
        );
    }
}
