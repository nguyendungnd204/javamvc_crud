package com.myjavaweb.javamvc_crud.Services;

import com.myjavaweb.javamvc_crud.Models.Product;
import com.myjavaweb.javamvc_crud.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> getAllProductsSortedByPriceDesc() {
        return repo.findAll().stream()
                .sorted((p1,p2)-> Double.compare(p2.getPrice(),p1.getPrice()))
                .toList();
    }
    public List<Product> getAllProductsSortedByPriceASC() {
        return repo.findAll().stream()
                .sorted((p1,p2)-> Double.compare(p1.getPrice(),p2.getPrice()))
                .toList();
    }
    public List<Product> searchProductsByName(String name) {
        return repo.findByName(name);
    }
}
