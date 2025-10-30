package com.dgramada.order;

import com.dgramada.customer.Customer;
import com.dgramada.customer.CustomerRepository;
import com.dgramada.exceptionhandling.exception.ResourceNotFoundException;
import com.dgramada.kafka.KafkaProducerService;
import com.dgramada.order.dto.OrderCreateRequest;
import com.dgramada.order.dto.OrderResponse;
import com.dgramada.order.model.Order;
import com.dgramada.product.Product;
import com.dgramada.product.ProductMapper;
import com.dgramada.product.ProductRepository;
import com.dgramada.schemas.events.OrderCreatedEvent;
import com.dgramada.schemas.events.ProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final KafkaProducerService kafkaProducerService;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, OrderMapper orderMapper, ProductMapper productMapper, KafkaProducerService kafkaProducerService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public OrderResponse save(OrderCreateRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Product> products = productRepository.findAllById(request.productIds());
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No valid products found for given IDs");
        }

        final var order = orderRepository.save(orderMapper.from(customer, products));

        final var response = orderMapper.toResponse(
                order,
                customer,
                products.stream().map(productMapper::toResponse).toList()
        );
        kafkaProducerService.send(buildOrderCreatedEvent(order, products));

        return response;
    }

    public Page<OrderResponse> getAll(Pageable pageable) {
        logger.info("Getting paged orders");
        return orderRepository.findAll(pageable).map(order -> orderMapper.toResponse(
                order,
                order.getCustomer(),
                order.getProducts().stream().map(productMapper::toResponse).toList()
        ));
    }

    public OrderResponse getById(Long id) {
        logger.info("Getting order with id {}", id);
        return orderRepository.findById(id)
                .map(order -> orderMapper.toResponse(
                        order,
                        order.getCustomer(),
                        order.getProducts().stream().map(productMapper::toResponse).toList()
                ))
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found"));
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order, List<Product> products) {
        final var productInfos = products.stream()
                .map(product -> ProductInfo.newBuilder()
                        .setProductId(product.getId())
                        .setName(product.getName())
                        .setPrice(ByteBuffer.wrap(product.getPrice().unscaledValue().toByteArray()))
                        .build()
                ).toList();

        return OrderCreatedEvent.newBuilder()
                .setEventId(UUID.randomUUID())
                .setOrderId(order.getId())
                .setCustomerId(order.getCustomer().getId())
                .setStatus(order.getStatus().name())
                .setProducts(productInfos)
                .setCreatedAt(order.getCreatedAt().toInstant(ZoneOffset.UTC))
                .build();
    }
}
