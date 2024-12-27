package com.myjavaweb.javamvc_crud.repo;

import com.myjavaweb.javamvc_crud.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where lower(p.name) like lower(concat('%',:name,'%') ) ")
    List<Product> findByName(String name);

}
