package com.example.shop.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponse {

    private String name;
    private Integer stock;
    private String color;
    private String size;
}
