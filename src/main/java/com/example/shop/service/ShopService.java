package com.example.shop.service;

import com.example.shop.DTO.AddResponse;
import com.example.shop.DTO.BuyResponse;
import com.example.shop.DTO.ProductRequest;
import com.example.shop.DTO.StockResponse;
import com.example.shop.exception.StockNotFoundException;

import java.util.List;

public interface ShopService {
    AddResponse addProduct(ProductRequest productRequest);

    List<StockResponse> checkStock();

    BuyResponse buyProducts(String name, String color, String size, Integer pieces) throws ClassCastException ;

}
