package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends CrudRepository<Product, Integer> {

    public Optional<Product> findAllByColorAndAndNameAndAndSize(String color, String name, String size);
}
