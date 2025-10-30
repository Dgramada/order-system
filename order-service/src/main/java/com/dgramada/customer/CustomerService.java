package com.dgramada.customer;

import com.dgramada.customer.dto.CustomerCreateRequest;
import com.dgramada.customer.dto.CustomerResponse;
import com.dgramada.exceptionhandling.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public void save(CustomerCreateRequest request) {
        logger.info("Saving customer {}", request);
        final var customer = customerMapper.from(request);
        customerRepository.save(customer);
    }

    public Page<CustomerResponse> getAll(Pageable pageable) {
        logger.info("Getting paged customers");
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }

    public CustomerResponse getById(Long id) {
        logger.info("Getting customer with id {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
    }
}
