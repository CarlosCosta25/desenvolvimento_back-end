package br.edu.ifmg.produto.util;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;

public class Factory {

    public static Product createProduct(){
        Product p = new Product();
        p.setName("IPhone XXX");
        p.setPrice(50000);
        p.setImageUrl("https://www.apple.com/v/iphone/home/ai/images/heroes/iphone-14-pro/hero_static__d1v0x2j4g7i6_large.jpg");
        p.getCategories()
                .add(new Category(1L,"Livros"));
        return p;
    }
    public static ProductDTO createProductDTO(){
        return new ProductDTO(createProduct());
    }
}
