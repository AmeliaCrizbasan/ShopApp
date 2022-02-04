package com.example.shop.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BuyResponse implements Serializable {
    private String message;
}

