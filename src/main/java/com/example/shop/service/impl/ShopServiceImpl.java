package com.example.shop.service.impl;

import com.example.shop.DTO.AddResponse;
import com.example.shop.DTO.BuyResponse;
import com.example.shop.DTO.ProductRequest;
import com.example.shop.DTO.StockResponse;
import com.example.shop.exception.StockNotFoundException;
import com.example.shop.model.Product;
import com.example.shop.model.Stock;
import com.example.shop.repository.ShopRepository;
import com.example.shop.repository.StockRepository;
import com.example.shop.service.ShopService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final StockRepository stockRepository;

    public ShopServiceImpl(ShopRepository shopRepository, StockRepository stockRepository) {
        this.shopRepository = shopRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public AddResponse addProduct(ProductRequest productRequest) {

        Product product = new Product();
        AddResponse responseMessage = new AddResponse();
        product.setName(productRequest.getName());
        product.setColor(productRequest.getColor());
        product.setSize(productRequest.getSize());
        product.setPrice(productRequest.getPrice());

        Optional<Product> prod = shopRepository.findAllByColorAndAndNameAndAndSize(productRequest.getColor(), productRequest.getName(),
                productRequest.getSize());

        if (prod.isPresent()) {
            Optional<Stock> stock = stockRepository.findAllByProduct(prod.get());
            if (stock.isPresent()) {
                Stock existingStock = stock.get();
                existingStock.setStock(existingStock.getStock() + 1);
                stockRepository.save(existingStock);

                responseMessage.setMessage(existingStock.getStock().toString());
//                return existingStock.getStock().toString();
            } else {
                Stock newStock = new Stock();
                newStock.setStock(1);
                newStock.setProduct(prod.get());
                stockRepository.save(newStock);

                responseMessage.setMessage("The product is no longer available");
                //return "The product is no longer available";
            }
        } else {
            Product response = shopRepository.save(product);
            Stock newStock = new Stock();
            newStock.setStock(1);
            newStock.setProduct(response);
            stockRepository.save(newStock);

            responseMessage.setMessage("The product does not exist");
            //return "The product does not exist";
        }

        return responseMessage;
    }

    public List<StockResponse> checkStock() {

        Iterable<Stock> allstocks = stockRepository.findAll();
        // List<StockResponse> stockResponses = Arrays.asList(allstocks).stream();
        List<StockResponse> stockResponses = new ArrayList<>();

        for (Stock s : allstocks) {
//              if(s.getStock()!=0) {
            StockResponse stockResponse = new StockResponse();
            stockResponse.setStock(s.getStock());
            stockResponse.setColor(s.getProduct().getColor());
            stockResponse.setSize(s.getProduct().getSize());
            stockResponse.setName(s.getProduct().getName());
            stockResponses.add(stockResponse);
//             } else { throw new StockNotFoundException(); }


        }

        return stockResponses;

    }

    public BuyResponse buyProducts(String name, String color, String size, Integer pieces) throws ClassCastException  {
        Optional<Product> prod = shopRepository.findAllByColorAndAndNameAndAndSize(color, name, size);
//        Optional<Product> prod = shopRepository.findAllByColorAndAndNameAndAndSize(request.getColor(), request.getName(),
//                request.getSize());
        BuyResponse response = new BuyResponse();
        if (prod.isPresent()) {
            Optional<Stock> stock = stockRepository.findAllByProduct(prod.get());
            Stock existingStock = stock.get();
            if (stock.isPresent() && existingStock.getStock() >= pieces) {

                existingStock.setStock(existingStock.getStock() - pieces);
                stockRepository.save(existingStock);
                response.setMessage("Buy");
                //return "Buy";

            } else {
                response.setMessage("The product is no longer available, stock: " + existingStock.getStock().toString());
                //return "The product is no longer available, "+ " "+ "stock: "+existingStock.getStock().toString();
            }
        } else {
            response.setMessage("The product does not exist");
            // return "The product does not exist";
        }
        return response;
    }


}
