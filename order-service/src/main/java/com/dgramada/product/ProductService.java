package com.dgramada.product;

import com.dgramada.exceptionhandling.exception.ResourceNotFoundException;
import com.dgramada.product.dto.ProductCreateRequest;
import com.dgramada.product.dto.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void save(ProductCreateRequest request) {
        logger.info("Saving product {}", request);
        final var product = productMapper.from(request);
        productRepository.save(product);
    }

    public Page<ProductResponse> getAll(Pageable pageable) {
        logger.info("Getting paged products");
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    public ProductResponse getById(Long id) {
        logger.info("Getting product with id {}", id);
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }
}
