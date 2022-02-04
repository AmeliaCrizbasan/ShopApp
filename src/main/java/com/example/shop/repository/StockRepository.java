package com.example.shop.repository;

import com.example.shop.model.Product;
import com.example.shop.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {
  public Optional<Stock> findAllByProduct(Product prod);
}
