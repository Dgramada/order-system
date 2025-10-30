package com.dgramada.product;

import com.dgramada.product.dto.ProductCreateRequest;
import com.dgramada.product.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product from(ProductCreateRequest request) {
        final var product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getName(),
                product.getPrice()
        );
    }
}
