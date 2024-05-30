package org.example.part_four.controllers;

import org.example.part_four.entity.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    private static List<Product> products = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id){
        isIndexValid(id);
        var product = products.get(id-1);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product productToAdd){
        products.add(productToAdd);
        return new ResponseEntity<>(productToAdd, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product updateData){
        isIndexValid(id);
        var product = products.get(id-1);
        copyUserProperties(updateData, product);
        products.set(id-1, product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int id){
        isIndexValid(id);
        products.remove(id-1);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.NO_CONTENT);
    }

    private void isIndexValid(int index){
        if(!(index <= products.size() && index > 0)){
            throw new NoSuchElementException("No product exist with the provided id");
        }
    }

    private void copyUserProperties(Product productDto, Product product) {
        for (Field field : Product.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(productDto);
                if(value != null){
                    Field userField = Product.class.getDeclaredField(field.getName());
                    userField.setAccessible(true);
                    userField.set(product, value);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error updating product properties", e);
            }
        }
    }
}
