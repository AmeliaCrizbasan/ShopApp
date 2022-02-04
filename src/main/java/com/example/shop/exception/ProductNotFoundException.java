package com.example.shop.exception;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(){
        super("Product not found!");
    }
}
