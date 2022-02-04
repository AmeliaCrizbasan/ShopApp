package com.example.shop.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {
    private LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private String message;

}
