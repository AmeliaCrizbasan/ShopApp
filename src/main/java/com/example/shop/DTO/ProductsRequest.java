package com.example.shop.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsRequest {
    private String name;
    private String color;
    private String size;
    private Integer pieces;
}
