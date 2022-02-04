package com.example.shop.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductResponse {
    private Integer id;
    private String name;
    private Double price;
    private String color;
    private String size;
}
