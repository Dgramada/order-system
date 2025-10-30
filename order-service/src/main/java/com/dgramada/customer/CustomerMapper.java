package com.dgramada.customer;

import com.dgramada.customer.dto.CustomerCreateRequest;
import com.dgramada.customer.dto.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer from(CustomerCreateRequest request) {
        final var customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        return customer;
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getName(),
                customer.getEmail()
        );
    }
}
