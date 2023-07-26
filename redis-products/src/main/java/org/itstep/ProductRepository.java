package org.itstep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    public static final String KEY = "Product";

    @Autowired
    private RedisTemplate redisTemplate;

    public Product save(Product product) {
        redisTemplate.opsForHash().put(KEY, product.getId(), product);
        return product;
    }

    public List<Product> findAll() {
        return redisTemplate.opsForHash().values(KEY);
    }

    public Product findProductById(int id) {
        return (Product) redisTemplate.opsForHash().get(KEY, id);
    }


    public String deleteProduct(int id) {
        redisTemplate.opsForHash().delete(KEY, id);
        return "product removed !!";
    }
}
