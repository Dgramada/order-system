package com.dgramada.order;

import com.dgramada.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = {"customer", "products"})
    @NonNull
    Page<Order> findAll(@NonNull Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"customer", "products"})
    @NonNull
    Optional<Order> findById(@NonNull Long id);
}
