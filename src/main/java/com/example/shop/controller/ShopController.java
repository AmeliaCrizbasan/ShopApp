package com.example.shop.controller;

import com.example.shop.DTO.AddResponse;
import com.example.shop.DTO.BuyResponse;
import com.example.shop.DTO.ProductRequest;
import com.example.shop.DTO.StockResponse;
import com.example.shop.exception.StockNotFoundException;
import com.example.shop.service.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<AddResponse> addProducts(@RequestBody ProductRequest request) throws ClassCastException  {

        return new ResponseEntity<>(shopService.addProduct(request), HttpStatus.OK);

    }

    @GetMapping(path = "/check")
    public ResponseEntity<List<StockResponse>> checkStock() {

        return new ResponseEntity<>(shopService.checkStock(), HttpStatus.OK);
    }

    @GetMapping("/buy/name/{name}/color/{color}/size/{size}/pieces/{pieces}")
    public ResponseEntity<BuyResponse> buyProducts(@PathVariable("name") String name,
                                                   @PathVariable("color") String color,
                                                   @PathVariable("size") String size,
                                                   @PathVariable("pieces") Integer pieces) throws ClassCastException{
        BuyResponse response = shopService.buyProducts(name, color, size, pieces);
        return new ResponseEntity<>(response, HttpStatus.OK);
        //  return new ResponseEntity<>(response, HttpStatus.OK);
//        if (response.getMessage()=="Buy") {
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        }
//        if (response.getMessage().equals("Buy")) {
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            throw new StockNotFoundException();
//
//        }
    }


}