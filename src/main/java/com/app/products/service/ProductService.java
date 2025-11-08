package com.app.products.service;

import com.app.products.rest.CreateProductRestModel;

import java.util.concurrent.ExecutionException;

public interface ProductService {

    String createProduct(CreateProductRestModel product) throws ExecutionException, InterruptedException;
}
