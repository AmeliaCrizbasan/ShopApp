package com.example.shop.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Message<T> implements Serializable {
    private String type;
    private T payload;
}
