package ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecom.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>{

    List<Product> findAllByCategoryId(int id);
}
