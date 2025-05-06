package br.edu.ifmg.produto.repository;

import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private  long existenteId;

    @BeforeEach
    void setUp() throws Exception{
        existenteId = 1L;

    }

    @Test
    @DisplayName(value = "Verificando se o objeto não existe no BD depois de deletado")
    public void deleteShouldDeleteObjectWhenIdExists(){
        productRepository.deleteById(existenteId);
        Optional<Product> obj =productRepository.findById(existenteId);
        Assertions.assertFalse(obj.isPresent());
    }

    @Test
    @DisplayName(value = "Verificando se o objeto existe no BD depois de deletado")
    public void insertShouldPersisitWithAutoIncrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(product.getId());
        Product p = productRepository.save(product);
        Optional<Product>obj = productRepository.findById(p.getId());
        Assertions.assertTrue(obj.isPresent());
        Assertions.assertNotEquals(0,obj.get().getId());
        Assertions.assertEquals(26,obj.get().getId());

    }


}
